package org.auioc.mods.itemexporter.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.itemexporter.command.IEClientCommands.FEEDBACK_HELPER;
import java.util.function.Function;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import org.auioc.mods.arnicalib.api.java.holder.ObjectHolder;
import org.auioc.mods.arnicalib.utils.game.CommandUtils;
import org.auioc.mods.arnicalib.utils.game.LanguageUtils;
import org.auioc.mods.itemexporter.config.IEConfig;
import net.minecraft.commands.CommandSourceStack;

public class ConfigCommand {

    public static final CommandNode<CommandSourceStack> NODE = literal("config")
        .then(
            literal("language")
                .executes(LanguageNodeHandler::list)
                .then(
                    literal("add")
                        .then(
                            argument("langCode", StringArgumentType.string())
                                .suggests(LanguageUtils.ALL_LANGUAGES_SUGGESTION)
                                .executes(LanguageNodeHandler::add)
                        )
                )
                .then(
                    literal("remove")
                        .then(
                            argument("langCode", StringArgumentType.string())
                                .suggests(IEConfig.LANGUAGES.getCommandSuggestion())
                                .executes(LanguageNodeHandler::remove)
                        )
                )
        )
        .then(
            literal("tag")
                .then(
                    literal("minecraftOnly")
                        .executes((ctx) -> get(ctx, IEConfig.MINECRAFT_TAG_ONLY))
                        .then(
                            argument("minecraftOnly", BoolArgumentType.bool())
                                .executes((ctx) -> set(ctx, IEConfig.MINECRAFT_TAG_ONLY, BoolArgumentType.getBool(ctx, "minecraftOnly")))
                        )
                )
        )
        .then(
            literal("export")
                .then(
                    literal("exportJsonToStdout")
                        .executes((ctx) -> get(ctx, IEConfig.EXPORT_JSON_TO_STDOUT))
                        .then(
                            argument("exportJsonToStdout", BoolArgumentType.bool())
                                .executes((ctx) -> set(ctx, IEConfig.EXPORT_JSON_TO_STDOUT, BoolArgumentType.getBool(ctx, "exportJsonToStdout")))
                        )
                )
        )
        .build();

    private static <T> int get(CommandContext<CommandSourceStack> ctx, ObjectHolder<T> configValue) {
        return FEEDBACK_HELPER.success(ctx, CommandUtils.joinLiteralNodes(ctx.getNodes(), 2) + ".get", configValue.get());
    }

    private static <T> int set(CommandContext<CommandSourceStack> ctx, ObjectHolder<T> configValue, T newValue) {
        configValue.set(newValue);
        return FEEDBACK_HELPER.success(ctx, CommandUtils.joinLiteralNodes(ctx.getNodes(), 2) + ".set", newValue);
    }

    private static class LanguageNodeHandler {

        private static int list(CommandContext<CommandSourceStack> ctx) {
            var sb = new StringBuffer();
            IEConfig.LANGUAGES.get().forEach((langCode, _v) -> {
                sb.append(langCode).append(", ");
            });
            sb.delete(sb.length() - 2, sb.length());

            return FEEDBACK_HELPER.success(ctx, "config.language.list", sb.toString());
        }

        private static int add(CommandContext<CommandSourceStack> ctx) {
            return processLangCodeArgument(ctx, (s) -> IEConfig.LANGUAGES.add(s), "config.language.add");
        }

        private static int remove(CommandContext<CommandSourceStack> ctx) {
            return processLangCodeArgument(ctx, (s) -> IEConfig.LANGUAGES.remove(s), "config.language.remove");
        }

        private static int processLangCodeArgument(CommandContext<CommandSourceStack> ctx, Function<String, Boolean> action, String messageKey) {
            var langCode = StringArgumentType.getString(ctx, "langCode");
            if (action.apply(langCode)) {
                return FEEDBACK_HELPER.success(ctx, messageKey, langCode);
            }
            return FEEDBACK_HELPER.failure(ctx, messageKey, langCode);
        }

    }

}
