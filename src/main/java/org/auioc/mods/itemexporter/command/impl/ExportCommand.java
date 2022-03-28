package org.auioc.mods.itemexporter.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.itemexporter.command.IEClientCommands.FEEDBACK_HELPER;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mods.arnicalib.common.command.argument.CreativeModeTabArgument;
import org.auioc.mods.arnicalib.utils.game.CommandUtils;
import org.auioc.mods.itemexporter.exporter.Exporter;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.ModIdArgument;

public class ExportCommand {

    public static final CommandNode<CommandSourceStack> NODE = literal("export")
        .then(literal("hand").executes(ExportCommand::hand))
        .then(literal("inventory").executes(ExportCommand::inventory))
        .then(
            literal("modId")
                .then(
                    argument("modId", ModIdArgument.modIdArgument())
                        .executes(ExportCommand::modId)
                )
        )
        .then(
            literal("creativeModeTab")
                .then(
                    argument("creativeModeTab", CreativeModeTabArgument.creativeModeTab())
                        .executes(ExportCommand::creativeModeTab)
                )
        )
        .build();

    private static int export(CommandContext<CommandSourceStack> ctx, List<Item> itemList) {
        Exporter.export(itemList);
        return FEEDBACK_HELPER.success(ctx, "export", itemList.size());
    }

    private static int hand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Exporter.export(CommandUtils.getLocalPlayerOrException(ctx.getSource()).getMainHandItem().getItem());

        return FEEDBACK_HELPER.success(ctx, "export", 1);
    }

    private static int inventory(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var itemList = CommandUtils.getLocalPlayerOrException(ctx.getSource())
            .getInventory().items
                .stream()
                .filter((itemStack) -> !itemStack.isEmpty())
                // Distinct by description id
                // .filter(((Function<Function<ItemStack, String>, Predicate<ItemStack>>) (keyExtractor) -> {
                //     var set = new HashSet<String>();
                //     return t -> set.add(keyExtractor.apply(t));
                // }).apply(ItemStack::getDescriptionId))
                .map(ItemStack::getItem)
                .distinct()
                .toList();

        return export(ctx, itemList);
    }

    private static int modId(CommandContext<CommandSourceStack> ctx) {
        final String modId = ctx.getArgument("modId", String.class);

        var itemList = ForgeRegistries.ITEMS.getEntries()
            .stream()
            .filter((entry) -> entry.getKey().getRegistryName().getNamespace().equals(modId))
            .map(Entry::getValue)
            .toList();

        return export(ctx, itemList);
    }

    private static int creativeModeTab(CommandContext<CommandSourceStack> ctx) {
        final CreativeModeTab tab = ctx.getArgument("creativeModeTab", CreativeModeTab.class);

        NonNullList<ItemStack> itemStacks = NonNullList.create();
        tab.fillItemList(itemStacks);

        var itemListnew = (new HashSet<Item>() {
            {
                for (ItemStack itemStack : itemStacks) {
                    add(itemStack.getItem());
                }
            }
        })
            .stream()
            .toList();

        return export(ctx, itemListnew);
    }

}
