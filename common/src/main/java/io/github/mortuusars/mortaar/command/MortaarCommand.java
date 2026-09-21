package io.github.mortuusars.mortaar.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.mortuusars.mortaar.Config;
import io.github.mortuusars.mortaar.bugger.Bugger;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class MortaarCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("mortaar")
              .requires((stack) -> stack.hasPermission(3))
              .then(Commands.literal("debug")
                    .executes(MortaarCommand::toggleDebugMode)
                    .then(Commands.literal("tests")
                          .executes(MortaarCommand::runTests))));
    }

    private static int toggleDebugMode(CommandContext<CommandSourceStack> context) {
        boolean newValue = !Config.Server.DEBUG_MODE.get();
        Config.Server.DEBUG_MODE.set(newValue);
        Config.Server.DEBUG_MODE.save();
        context.getSource().sendSuccess(() -> Component.literal("Debug mode: " + (newValue ? "Enabled" : "Disabled")).withStyle(ChatFormatting.GRAY), true);
        return 0;
    }

    private static int runTests(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Bugger.runTests(
              context.getSource().getPlayerOrException(),
              message -> context.getSource().sendSuccess(() -> message, true));
        return 0;
    }
}
