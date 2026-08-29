package io.github.mortuusars.mortaar;

import net.neoforged.neoforge.common.ModConfigSpec;

public abstract class Config {
    public static class Server {
        public static final ModConfigSpec SPEC;

        public static final ModConfigSpec.BooleanValue DEBUG_MODE;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            DEBUG_MODE = builder
                  .comment("Enable debug features. Will negatively impact performance. Don't enable unless it's needed.")
                  .define("debug_mode", false);

            SPEC = builder.build();
        }
    }
}