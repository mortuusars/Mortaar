package io.github.mortuusars.seal;

import dev.architectury.injectables.annotations.ExpectPlatform;

import java.nio.file.Path;

public class Platform {
    @ExpectPlatform
    public static boolean isInDevEnv() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModLoaded(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean isModLoading(String modId) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Path getGameDirectory() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Path getConfigDirectory() {
        throw new AssertionError();
    }
}
