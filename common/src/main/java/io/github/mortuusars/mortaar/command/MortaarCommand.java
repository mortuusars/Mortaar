package io.github.mortuusars.mortaar.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mortuusars.mortaar.Config;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class MortaarCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
//        dispatcher.register(Commands.literal("mortaar")
//              .requires((stack) -> stack.hasPermission(3))
//              .then(Commands.literal("debug")
//                    .executes(MortaarCommand::toggleDebugMode)
//                    .then(Commands.literal("test")
//                          .executes(MortaarCommand::test))));
    }

//    private static int toggleDebugMode(CommandContext<CommandSourceStack> context) {
//        boolean newValue = !Config.Server.DEBUG_MODE.get();
//        Config.Server.DEBUG_MODE.set(newValue);
//        Config.Server.DEBUG_MODE.save();
//        context.getSource().sendSuccess(() -> Component.literal("Debug mode: " + (newValue ? "Enabled" : "Disabled")).withStyle(ChatFormatting.GRAY), true);
//        return 0;
//    }

    private static int test(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        return 0;
    }
}
