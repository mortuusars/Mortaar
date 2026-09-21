package io.github.mortuusars.mortaar.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.serialization.DataResult;
import io.github.mortuusars.mortaar.util.GameTimeId;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class GameTimeIdArgument implements ArgumentType<GameTimeId> {
    protected GameTimeIdArgument() {
    }

    public static GameTimeIdArgument id() {
        return new GameTimeIdArgument();
    }

    public static GameTimeId getId(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
        return context.getArgument(name, GameTimeId.class);
    }

    @Override
    public GameTimeId parse(StringReader reader) throws CommandSyntaxException {
        String string = reader.readString();
        DataResult<GameTimeId> parseResult = GameTimeId.parseFromHex(string);

        if (parseResult.isError()) {
            throw new SimpleCommandExceptionType(Component.literal(parseResult.error().orElseThrow().message())).create();
        }

        return parseResult.getOrThrow();
    }
}