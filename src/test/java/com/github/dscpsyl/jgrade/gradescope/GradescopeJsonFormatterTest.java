package com.github.dscpsyl.jgrade.gradescope;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.github.dscpsyl.jgrade.Grader;
import com.github.dscpsyl.jgrade.gradedtest.GradedTestResult;

public class GradescopeJsonFormatterTest {

    private GradescopeJsonFormatter unit;
    private Grader grader;

    @Before
    public void initUnit() {
        unit = new GradescopeJsonFormatter();
        grader = new Grader();
    }

    private static void assertValidJson(String s) throws JSONException {
        new JSONObject(s);
    }

    @Test(expected=GradescopeJsonException.class)
    public void invalidIfEmpty() {
        unit.format(grader);
    }

    @Test(expected=GradescopeJsonException.class)
    public void invalidIfNoTestsOrScore() {
        grader.setExecutionTime(45);
        unit.format(grader);
    }

    @Test
    public void validIfScoreSet() throws JSONException {
        grader.setScore(20.0);
        assertValidJson(unit.format(grader));
    }

    @Test
    public void validIfTests() throws JSONException {
        grader.addGradedTestResult(new GradedTestResult("", "", 20.0, "visible"));
        assertValidJson(unit.format(grader));
    }

    @Test(expected=GradescopeJsonException.class)
    public void catchesInvalidVisibility() {
        unit.setVisibility("invisible");
    }

    @Test(expected=GradescopeJsonException.class)
    public void catchesInvalidStdoutVisibility() {
        unit.setStdoutVisibility("invisible");
    }
}
