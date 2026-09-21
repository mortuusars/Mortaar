package io.github.mortuusars.mortaar.bugger.test;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import net.minecraft.server.level.ServerPlayer;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BuggerTests {
    public static final Logger LOGGER = LogUtils.getLogger();
    private final List<Test> tests = new ArrayList<>();

    public BuggerTests add(Test test) {
        tests.add(test);
        return this;
    }

    public BuggerTests add(String name, Function<ServerPlayer, DataResult<Boolean>> function) {
        return add(new Test(name, function));
    }

    public BuggerTests addFrom(BuggerTests tests) {
        this.tests.addAll(tests.tests);
        return this;
    }

    // --

    public TestResults run(ServerPlayer player, Consumer<Integer> testCount) {
        testCount.accept(tests.size());

        List<String> passed = new ArrayList<>();
        List<TestResults.Result> failed = new ArrayList<>();

        for (Test test : tests) {
            try {
                test.function().apply(player).error().ifPresentOrElse(
                      err -> failed.add(new TestResults.Result(test.name(), err.message())),
                      () -> passed.add(test.name()));
            } catch (Exception e) {
                failed.add(new TestResults.Result(test.name(), "Test has thrown an unexpected exception: " + e.getMessage()));
            }
        }

        return new TestResults(passed, failed);
    }
}
