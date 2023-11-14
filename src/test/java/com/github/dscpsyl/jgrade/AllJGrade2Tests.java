package com.github.dscpsyl.jgrade;


import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.junit.runners.Suite.SuiteClasses;

import com.github.dscpsyl.jgrade.gradedtest.AllGraderTests;
import com.github.dscpsyl.jgrade.gradescope.GradescopeJsonFormatterTest;

/* A test suite shortcut to run all tests in jGrade2 */
@Suite
@SuiteDisplayName("All JGrade2 Tests")
@SuiteClasses({
        GraderTest.class,
        JGradeCommandLineTest.class,
        AllGraderTests.class,
        GradescopeJsonFormatterTest.class,
        CLITesterExecutionResultTest.class,
        DeductiveGraderStrategyTest.class,
})
public class AllJGrade2Tests { }
