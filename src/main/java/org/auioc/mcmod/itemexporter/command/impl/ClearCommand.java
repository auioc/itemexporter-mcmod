package org.auioc.mcmod.itemexporter.command.impl;

import static net.minecraft.commands.Commands.literal;
import static org.auioc.mcmod.itemexporter.command.IEClientCommands.FEEDBACK_HELPER;
import java.io.File;
import java.io.IOException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import org.apache.commons.io.FileUtils;
import net.minecraft.commands.CommandSourceStack;

public class ClearCommand {

    public static final CommandNode<CommandSourceStack> NODE = literal("clear")
        .executes(ClearCommand::clear)
        .build();

    private static int clear(CommandContext<CommandSourceStack> ctx) {
        try {
            FileUtils.deleteDirectory(new File("export"));
        } catch (IOException e) {
            e.printStackTrace();
            return FEEDBACK_HELPER.failure(ctx, "clear");
        }

        return FEEDBACK_HELPER.success(ctx, "clear");
    }
}
