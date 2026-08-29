package io.github.mortuusars.mortaar.bugger;

import java.util.function.Supplier;

/**
 * Utility to make in-game debugging easier.
 */
public class Bugger {
    public static Supplier<Boolean> enabler = () -> false;

    public static boolean isEnabled() {
        return enabler.get();
    }
}