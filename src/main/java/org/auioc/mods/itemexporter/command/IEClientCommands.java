package org.auioc.mods.itemexporter.command;

import static net.minecraft.commands.Commands.literal;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mods.arnicalib.client.command.AHClientCommands;
import org.auioc.mods.arnicalib.common.command.impl.VersionCommand;
import org.auioc.mods.arnicalib.utils.game.CommandUtils.CommandFeedbackHelper;
import org.auioc.mods.itemexporter.ItemExporter;
import org.auioc.mods.itemexporter.command.impl.ConfigCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;

@OnlyIn(Dist.CLIENT)
public class IEClientCommands {

    private static final CommandNode<CommandSourceStack> NODE = literal(ItemExporter.MOD_ID).build();

    public static final CommandFeedbackHelper FEEDBACK_HELPER = new CommandFeedbackHelper(ItemExporter::i18n);

    public static void register(final RegisterClientCommandsEvent event) {
        NODE.addChild(literal("version").executes((ctx) -> VersionCommand.getModVersion(ctx, ItemExporter.MAIN_VERSION, ItemExporter.FULL_VERSION, ItemExporter.MOD_NAME)).build());

        NODE.addChild(ConfigCommand.NODE);

        AHClientCommands.getRootNode(event.getDispatcher()).addChild(NODE);
    }

}
