package io.github.mortuusars.mortaar.bugger.test.cases;

import io.github.mortuusars.mortaar.bugger.test.BuggerTests;

public class MortaarBuggerTests {
    public static BuggerTests createTests() {
        return new BuggerTests()
              .addFrom(new GTIDTests());
    }
}
