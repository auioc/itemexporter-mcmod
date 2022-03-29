package org.auioc.mods.itemexporter.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.itemexporter.command.IEClientCommands.FEEDBACK_HELPER;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mods.arnicalib.api.java.holder.BooleanHolder;
import org.auioc.mods.arnicalib.api.java.holder.IntegerHolder;
import org.auioc.mods.arnicalib.api.java.holder.ObjectHolder;
import org.auioc.mods.arnicalib.utils.game.CommandUtils;
import org.auioc.mods.itemexporter.config.IEConfig;
import net.minecraft.commands.CommandSourceStack;

public class ConfigCommand {

    public static final CommandNode<CommandSourceStack> NODE = literal("config")
        .then(
            literal("json")
                .then(ConfigDisplayNameLanguageCommand.BUILDER)
                .then(createBooleanConfigNode("includeTag", IEConfig.JSON_INCLUDE_TAG))
                .then(createBooleanConfigNode("includeDisplayName", IEConfig.JSON_INCLUDE_DISPLAY_NAME))
                .then(createBooleanConfigNode("includeCreativeTab", IEConfig.JSON_INCLUDE_CREATIVE_TAB))
                .then(createBooleanConfigNode("minecraftTagOnly", IEConfig.MINECRAFT_TAG_ONLY))
        )
        .then(
            literal("image")
                .then(createIntegerConfigNode("size", IEConfig.IMAGE_SIZE))
        )
        .then(
            literal("export")
                .then(createBooleanConfigNode("exportJson", IEConfig.EXPORT_JSON))
                .then(createBooleanConfigNode("exportImage", IEConfig.EXPORT_IMAGE))
                .then(createBooleanConfigNode("exportJsonToStdout", IEConfig.EXPORT_JSON_TO_STDOUT))
        )
        .build();


    private static LiteralArgumentBuilder<CommandSourceStack> createBooleanConfigNode(String name, BooleanHolder configValue) {
        return literal(name)
            .executes((ctx) -> ConfigNodeUtils.get(ctx, configValue))
            .then(argument(name, BoolArgumentType.bool()).executes((ctx) -> ConfigNodeUtils.setBoolean(ctx, configValue)));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createIntegerConfigNode(String name, IntegerHolder configValue) {
        return literal(name)
            .executes((ctx) -> ConfigNodeUtils.get(ctx, configValue))
            .then(argument(name, IntegerArgumentType.integer()).executes((ctx) -> ConfigNodeUtils.setInteger(ctx, configValue)));
    }

    private static class ConfigNodeUtils {

        private static <T> int get(CommandContext<CommandSourceStack> ctx, ObjectHolder<T> configValue) {
            return FEEDBACK_HELPER.success(ctx, CommandUtils.joinLiteralNodes(ctx.getNodes(), 2) + ".get", configValue.get());
        }

        private static <T> int set(CommandContext<CommandSourceStack> ctx, ObjectHolder<T> configValue, T newValue) {
            configValue.set(newValue);
            return FEEDBACK_HELPER.success(ctx, CommandUtils.joinLiteralNodes(ctx.getNodes(), 2) + ".set", newValue);
        }

        private static int setBoolean(CommandContext<CommandSourceStack> ctx, BooleanHolder configValue) {
            return set(ctx, configValue, BoolArgumentType.getBool(ctx, getLastNodeName(ctx)));
        }

        private static int setInteger(CommandContext<CommandSourceStack> ctx, IntegerHolder configValue) {
            return set(ctx, configValue, IntegerArgumentType.getInteger(ctx, getLastNodeName(ctx)));
        }

        private static String getLastNodeName(CommandContext<CommandSourceStack> ctx) {
            var nodes = ctx.getNodes();
            return nodes.get(nodes.size() - 1).getNode().getName();
        }

    }

}
