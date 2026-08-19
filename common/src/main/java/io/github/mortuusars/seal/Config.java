package io.github.mortuusars.seal;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Using ForgeConfigApiPort on fabric allows using forge config in both environments and without extra dependencies on forge.
 */
public abstract class Config {
    public static class Common {
        public static final ModConfigSpec SPEC;

        public static final ModConfigSpec.BooleanValue TEST_VALUE;

        static {
            ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

            TEST_VALUE = builder
                  .comment("Test")
                  .define("test", true);

            SPEC = builder.build();
        }
    }
}