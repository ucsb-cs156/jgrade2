package com.github.dscpsyl.jgrade2.gradescope;

import org.json.JSONException;
import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import com.github.dscpsyl.jgrade2.Grader;
import com.github.dscpsyl.jgrade2.gradedtest.GradedTestResult;

import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GradescopeJsonFormatterTest {

    private GradescopeJsonFormatter unit;
    private Grader grader;

    @BeforeEach
    public void initUnit() {
        unit = new GradescopeJsonFormatter();
        grader = new Grader();
    }

    private static void assertValidJson(String s) throws JSONException {
        new JSONObject(s);
    }

    @Test
    public void invalidIfEmpty() {
        assertThrows(GradescopeJsonException.class, () -> {
            unit.format(grader);
        });
    }

    @Test
    public void invalidIfNoTestsOrScore() {
        assertThrows(GradescopeJsonException.class, () -> {
            grader.setExecutionTime(45);
            unit.format(grader);
        });
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

    @Test
    public void catchesInvalidVisibility() {
        assertThrows(GradescopeJsonException.class, () -> {
        unit.setVisibility("invisible");
        });
    }

    @Test
    public void catchesInvalidStdoutVisibility() {
        assertThrows(GradescopeJsonException.class, () -> {
        unit.setStdoutVisibility("invisible");
        });
    }
}
