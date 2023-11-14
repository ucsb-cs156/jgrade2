package com.github.dscpsyl.jgrade.gradedtest;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName; 

/* An all in one suite shortcut to run all tests related to GradedTest */
@Suite
@SuiteDisplayName("All Grader Tests")
@SelectClasses({
        GradedTestListenerTest.class,
        GradedTestResultTest.class,
})
public class AllGraderTests { }
