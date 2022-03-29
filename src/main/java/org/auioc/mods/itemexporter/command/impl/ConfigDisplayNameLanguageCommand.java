package org.auioc.mods.itemexporter.command.impl;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static org.auioc.mods.itemexporter.command.IEClientCommands.FEEDBACK_HELPER;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import org.auioc.mods.arnicalib.client.command.argument.LanguageInfoArgument;
import org.auioc.mods.itemexporter.config.IEConfig;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.commands.CommandSourceStack;

public class ConfigDisplayNameLanguageCommand {

    public static final LiteralArgumentBuilder<CommandSourceStack> BUILDER = literal("displayNameLanguage")
        .executes(ConfigDisplayNameLanguageCommand::list)
        .then(
            literal("add")
                .then(
                    argument("langInfo", LanguageInfoArgument.languageInfo())
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

        return feedback(ctx, true, "list", sb.toString());
    }

    private static int add(CommandContext<CommandSourceStack> ctx) {
        var langInfo = ctx.getArgument("langInfo", LanguageInfo.class);
        return feedback(ctx, IEConfig.DISPLAY_NAME_LANGUAGE.add(langInfo), "add", langInfo.getCode());
    }

    private static int remove(CommandContext<CommandSourceStack> ctx) {
        var langCode = StringArgumentType.getString(ctx, "langCode");
        return feedback(ctx, IEConfig.DISPLAY_NAME_LANGUAGE.remove(langCode), "remove", langCode);
    }

    private static int feedback(CommandContext<CommandSourceStack> ctx, boolean result, String actionName, String arg) {
        var key = "config.json.display_name_language." + actionName;
        if (result) {
            return FEEDBACK_HELPER.success(ctx, key, arg);
        }
        return FEEDBACK_HELPER.failure(ctx, key, arg);
    }

}
