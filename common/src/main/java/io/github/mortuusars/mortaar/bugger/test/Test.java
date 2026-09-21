package io.github.mortuusars.mortaar.bugger.test;

import com.mojang.serialization.DataResult;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;
import java.util.function.Function;

public record Test(String name, Function<ServerPlayer, DataResult<Boolean>> function) {
    public static Function<ServerPlayer, DataResult<Boolean>> isTrue(Function<ServerPlayer, Boolean> function) {
        return player -> function.apply(player)
              ? DataResult.success(true)
              : DataResult.error(() -> "Expected 'true', was 'false'.");
    }

    public static Function<ServerPlayer, DataResult<Boolean>> isFalse(Function<ServerPlayer, Boolean> function) {
        return player -> !function.apply(player)
              ? DataResult.success(true)
              : DataResult.error(() -> "Expected 'false', was 'true'.");
    }

    public static Function<ServerPlayer, DataResult<Boolean>> equals(Object expected, Function<ServerPlayer, Object> function) {
        return player -> {
            Object actual = function.apply(player);
            return actual.equals(expected)
                  ? DataResult.success(true)
                  : DataResult.error(() -> "Expected '" + expected + "', got '" + actual + "'.");
        };
    }

    public static Function<ServerPlayer, DataResult<Boolean>> notEquals(Object notExpected, Function<ServerPlayer, Object> function) {
        return player -> {
            Object actual = function.apply(player);
            return actual.equals(notExpected)
                  ? DataResult.success(true)
                  : DataResult.error(() -> "Expected different from '" + notExpected + "', got equal '" + actual + "'.");
        };
    }

    public static Function<ServerPlayer, DataResult<Boolean>> throwsException(Class<? extends Exception> exceptionClass, Consumer<ServerPlayer> function) {
        return player -> {
            try {
                function.accept(player);
                return DataResult.error(() -> "Expected thrown exception of '"+ exceptionClass.getName() + "', but no exception was thrown.");
            } catch (Exception e) {
                if (exceptionClass.isInstance(e)) {
                    return DataResult.success(true);
                }
                return DataResult.error(() -> "Expected thrown exception of '"+ exceptionClass.getName() + "', but got '" + e.getClass().getName() + "' instead.");
            }
        };
    }
}
