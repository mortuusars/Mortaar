package io.github.mortuusars.mortaar.bugger;

import io.github.mortuusars.mortaar.bugger.test.BuggerTests;
import io.github.mortuusars.mortaar.bugger.test.TestResults;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utility to make in-game debugging easier.
 */
public class Bugger {
    public static Supplier<Boolean> enabler = () -> false;
    public static boolean RunningTests = false;

    private static Collection<Supplier<BuggerTests>> tests = Collections.synchronizedCollection(new ArrayList<>());

    public static boolean isEnabled() {
        return enabler.get();
    }

    public static void addTests(Supplier<BuggerTests> testsSupplier) {
        tests.add(testsSupplier);
    }

    public static void runTests(ServerPlayer player, Consumer<Component> reporter) {
        if (RunningTests) {
            reporter.accept(Component.literal("Tests are already running.").withStyle(ChatFormatting.RED));
            return;
        }

        RunningTests = true;
        BuggerTests allTests = new BuggerTests();
        for (Supplier<BuggerTests> testsSupplier : tests) {
            allTests.addFrom(testsSupplier.get());
        }

        TestResults results = allTests.run(player, count -> reporter.accept(Component.literal("Running " + count + " bugger tests.")));

        reporter.accept(Component.literal("Bugger tests finished:"));

        if (results.failed().isEmpty()) {
            reporter.accept(Component.literal("All tests are passed!")
                  .withStyle(ChatFormatting.GREEN));
        } else {
            reporter.accept(Component.literal("Passed: " + results.passed().size() + "\n"));
            reporter.accept(Component.literal("Failed: " + results.failed().size() + ":")
                  .withStyle(ChatFormatting.RED));

            results.failed().forEach(failedTest -> {
                reporter.accept(Component.literal(" " + failedTest.name() + ": " + failedTest.error())
                      .withStyle(ChatFormatting.RED));
            });
        }
        RunningTests = false;
    }
}