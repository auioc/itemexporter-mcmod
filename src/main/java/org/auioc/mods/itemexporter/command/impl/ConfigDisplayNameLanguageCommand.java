package org.auioc.mods.itemexporter.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.itemexporter.command.IEClientCommands.FEEDBACK_HELPER;
import java.util.function.Function;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import org.auioc.mods.arnicalib.utils.game.LanguageUtils;
import org.auioc.mods.itemexporter.config.IEConfig;
import net.minecraft.commands.CommandSourceStack;

public class ConfigDisplayNameLanguageCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> BUILDER = literal("displayNameLanguage")
        .executes(ConfigDisplayNameLanguageCommand::list)
        .then(
            literal("add")
                .then(
                    argument("langCode", StringArgumentType.string())
                        .suggests(LanguageUtils.ALL_LANGUAGES_SUGGESTION)
                        .executes(ConfigDisplayNameLanguageCommand::add)
                )
        )
        .then(
            literal("remove")
                .then(
                    argument("langCode", StringArgumentType.string())
                        .suggests(IEConfig.DISPLAY_NAME_LANGUAGE.getCommandSuggestion())
                        .executes(ConfigDisplayNameLanguageCommand::remove)
                )
        );

    private static int list(CommandContext<CommandSourceStack> ctx) {
        var sb = new StringBuffer();
        IEConfig.DISPLAY_NAME_LANGUAGE.get().forEach((langCode, _v) -> {
            sb.append(langCode).append(", ");
        });
        sb.delete(sb.length() - 2, sb.length());

        return FEEDBACK_HELPER.success(ctx, "config.json.display_name_language.list", sb.toString());
    }

    private static int add(CommandContext<CommandSourceStack> ctx) {
        return processLangCodeArgument(ctx, (s) -> IEConfig.DISPLAY_NAME_LANGUAGE.add(s), "config.json.display_name_language.add");
    }

    private static int remove(CommandContext<CommandSourceStack> ctx) {
        return processLangCodeArgument(ctx, (s) -> IEConfig.DISPLAY_NAME_LANGUAGE.remove(s), "config.json.display_name_language.remove");
    }

    private static int processLangCodeArgument(CommandContext<CommandSourceStack> ctx, Function<String, Boolean> action, String messageKey) {
        var langCode = StringArgumentType.getString(ctx, "langCode");
        if (action.apply(langCode)) {
            return FEEDBACK_HELPER.success(ctx, messageKey, langCode);
        }
        return FEEDBACK_HELPER.failure(ctx, messageKey, langCode);
    }

}