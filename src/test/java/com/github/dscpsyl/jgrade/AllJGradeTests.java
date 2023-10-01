package com.github.dscpsyl.jgrade;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.github.dscpsyl.jgrade.gradedtest.AllGraderTests;
import com.github.dscpsyl.jgrade.gradescope.GradescopeJsonFormatterTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        GraderTest.class,
        JGradeCommandLineTest.class,
        AllGraderTests.class,
        GradescopeJsonFormatterTest.class,
        CLITesterExecutionResultTest.class,
        DeductiveGraderStrategyTest.class,
})
public class AllJGradeTests { }
