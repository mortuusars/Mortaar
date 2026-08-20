package io.github.mortuusars.mortaar.fabric;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class PlatformImpl {
    public static boolean isInDevEnv() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    /**
     * This method is here because on forge checking if mod is loaded at mixin apply time is different (LoadingModList vs ModList)
     * But on fabric we can use the same code.
     */
    public static boolean isModLoading(String modId) {
        return isModLoaded(modId);
    }

    public static Path getGameDirectory() {
        return FabricLoader.getInstance().getGameDir();
    }

    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
