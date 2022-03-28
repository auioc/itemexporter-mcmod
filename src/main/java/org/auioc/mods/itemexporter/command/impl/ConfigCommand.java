package org.auioc.mods.itemexporter.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.itemexporter.command.IEClientCommands.FEEDBACK_HELPER;
import java.util.function.Function;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
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
                        .executes(TagNodeHandler::getMinecraftOnly)
                        .then(
                            argument("minecraftOnly", BoolArgumentType.bool())
                                .executes(TagNodeHandler::setMinecraftOnly)
                        )
                )
        )
        .build();


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

    private static class TagNodeHandler {

        private static int getMinecraftOnly(CommandContext<CommandSourceStack> ctx) {
            return FEEDBACK_HELPER.success(ctx, "config.tag.minecraft_only.get", IEConfig.MINECRAFT_TAG_ONLY.get());
        }

        private static int setMinecraftOnly(CommandContext<CommandSourceStack> ctx) {
            boolean b = BoolArgumentType.getBool(ctx, "minecraftOnly");

            IEConfig.MINECRAFT_TAG_ONLY.set(b);

            return FEEDBACK_HELPER.success(ctx, "config.tag.minecraft_only.set", b);
        }

    }

}
