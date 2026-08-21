package io.github.mortuusars.mortaar;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Using ForgeConfigApiPort on fabric allows using forge config in both environments and without extra dependencies on forge.
 */
public abstract class Config {
    public static class Server {
        public static final ModConfigSpec SPEC;

//        public static final ModConfigSpec.BooleanValue DEBUG_MODE;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

//            DEBUG_MODE = builder
//                  .comment("Enable debug features. Will negatively impact performance. Don't enable unless it's needed.")
//                  .define("debug_mode", false);

            SPEC = builder.build();
        }
    }
}