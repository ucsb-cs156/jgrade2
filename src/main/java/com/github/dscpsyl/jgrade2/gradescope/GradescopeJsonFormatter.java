package com.github.dscpsyl.jgrade2.gradescope;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.dscpsyl.jgrade2.Grader;
import com.github.dscpsyl.jgrade2.OutputFormatter;
import com.github.dscpsyl.jgrade2.gradedtest.GradedTestResult;

import static com.github.dscpsyl.jgrade2.gradedtest.GradedTestResult.AFTER_DUE_DATE;
import static com.github.dscpsyl.jgrade2.gradedtest.GradedTestResult.AFTER_PUBLISHED;
import static com.github.dscpsyl.jgrade2.gradedtest.GradedTestResult.HIDDEN;
import static com.github.dscpsyl.jgrade2.gradedtest.GradedTestResult.VISIBLE;

import java.util.List;


/**
 * A concrete formatter for a {@link Grader} where the output it produces
 * is the JSON a Gradescope Autograder can work with.
 */
public class GradescopeJsonFormatter implements OutputFormatter {

    private static final String EXECUTION_TIME = "execution_time";
    private static final String STDOUT_VISIBILITY = "stdout_visibility";
    private static final String TESTS = "tests";
    private static final String SCORE = "score";
    private static final String MAX_SCORE = "max_score";
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String OUTPUT = "output";
    private static final String VISIBILITY = "visibility";

    private JSONObject json;
    private int prettyPrint;
    private String visibility;
    private String stdoutVisibility;

    /**
     * Creates an instance of the formatter. By default the pretty-print
     * option is off (the integer is negative).
     */
    public GradescopeJsonFormatter() {
        this.json = new JSONObject();
        this.prettyPrint = -1;
    }

    /**
     * Returns whether or not the formatter has a visibility set.
     * @return True if the formatter has a visibility set, false otherwise.
     */
    private boolean hasVisibility() {
        return this.visibility != null;
    }

    /**
     * Returns whether or not the formatter has a visibility set for standard out.
     * @return True if the formatter has a visibility set for standard out, false otherwise.
     */
    private boolean hasStdoutVisibility() {
        return this.stdoutVisibility != null;
    }

    // <editor-fold "desc="accessors">

    /**
     * Sets the visibility for all of the test cases.
     * @param visibility The top-level visibility to use for all test cases.
     * @throws GradescopeJsonException If visibility not valid.
     */
    public void setVisibility(String visibility) throws GradescopeJsonException {
        if (!isValidVisibility(visibility)) {
            throw new GradescopeJsonException(visibility + " is not a valid visibility");
        }
        this.visibility = visibility;
    }

    /**
     * Sets the visibility for standard out during the run.
     * @param visibility The visibility to set for standard out.
     * @throws GradescopeJsonException If visibility is not valid.
     */
    public void setStdoutVisibility(String visibility) throws GradescopeJsonException {
        if (!isValidVisibility(visibility)) {
            throw new GradescopeJsonException(visibility + " is not a valid visibility");
        }
        this.stdoutVisibility = visibility;
    }

    /**
     * Sets the pretty-print for the JSON to output. The integer is how many
     * spaces to add for each indent level. A negative integer corresponds to
     * disabling pretty-print. If non-negative, simply calls
     * {@link JSONObject#toString(int)}
     * @param prettyPrint The integer for how much to indent
     */
    public void setPrettyPrint(int prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    // </editor-fold>

    /**
     * Formats the {@link Grader} into a JSON string.
     * @param grader The grader to format.
     * @return The JSON string.
     * @throws GradescopeJsonException If the grader is not valid.
     */
    @Override
    public String format(Grader grader) {
        json = new JSONObject();
        this.assemble(grader, json);
        try {
            return this.prettyPrint >= 0 ? this.json.toString(this.prettyPrint) : this.json.toString();
        } catch (JSONException e) {
            throw new InternalError(e);
        }
    }

    /**
     * Assembles a {@link GradedTestResult} into a JSON object.
     * @param r The result to assemble.
     * @return The JSON object.
     * @throws GradescopeJsonException If the result is not valid.
     */
    private JSONObject assemble(GradedTestResult r) {
        try {
            return new JSONObject()
                    .put(NAME, r.getName())
                    .put(SCORE, r.getScore())
                    .put(MAX_SCORE, r.getPoints())
                    .put(NUMBER, r.getNumber())
                    .put(OUTPUT, r.getOutput())
                    .put(VISIBILITY, r.getVisibility());
        } catch (JSONException e) {
            throw new InternalError(e);
        }
    }

    /**
     * Assembles a list of {@link GradedTestResult}s into a JSON array.
     * @param l The list of results to assemble.
     * @return The JSON array.
     * @see #assemble(GradedTestResult)
     */
    private JSONArray assemble(List<GradedTestResult> l) {
        JSONArray testResults = new JSONArray();
        for (GradedTestResult r : l) {
            testResults.put(assemble(r));
        }
        return testResults;
    }

    /**
     * Assembles a {@link Grader} into a JSON object.
     * @param grader The grader to assemble.
     * @param json The JSON object to assemble into.
     * @throws GradescopeJsonException If the grader is not valid.
     */
    private void assemble(Grader grader, JSONObject json) throws GradescopeJsonException {
        try {
            validateGrader(grader);
            if (grader.hasScore()) {
                json.put(SCORE, grader.getScore());
            }
            if (grader.hasMaxScore()) {
                json.put(MAX_SCORE, grader.getMaxScore());
            }
            if (grader.hasExecutionTime()) {
                json.put(EXECUTION_TIME, grader.getExecutionTime());
            }
            if (grader.hasOutput()) {
                json.put(OUTPUT, grader.getOutput());
            }
            if (this.hasVisibility()) {
                json.put(VISIBILITY, this.visibility);
            }
            if (this.hasStdoutVisibility()) {
                json.put(STDOUT_VISIBILITY, this.stdoutVisibility);
            }
            if (grader.hasGradedTestResults()) {
                json.put(TESTS, this.assemble(grader.getGradedTestResults()));
            }
        } catch (JSONException e) {
            throw new InternalError(e);
        }
    }

    /**
     * Validates a {@link Grader} to make sure it is valid for the formatter.
     * @param grader The grader to validate.
     * @throws GradescopeJsonException If the grader is not valid.
     */
    private void validateGrader(Grader grader) {
        if (!(grader.hasScore() || grader.hasGradedTestResults())) {
            throw new GradescopeJsonException("Gradescope Json must have either tests or score set");
        }

        /* The following checks ~should~ all pass because they would have been checked when set. */
        assert isValidVisibility(this.visibility);
        assert isValidVisibility(this.stdoutVisibility);
        assert allValidVisibility(grader.getGradedTestResults());
    }

    /**
     * Checks if all of the {@link GradedTestResult}s have valid visibilities.
     * @param results The list of results to check.
     * @return True if all of the visibilities are valid, false otherwise.
     */
    private static boolean allValidVisibility(List<GradedTestResult> results) {
        for (GradedTestResult r : results) {
            if (!isValidVisibility(r.getVisibility())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a visibility is valid.
     * @param visibility The visibility to check.
     * @return True if the visibility is valid, false otherwise.
     */
    private static boolean isValidVisibility(String visibility) {
        return visibility == null  // Just wasn't set, which is OK
                || visibility.equals(VISIBLE)
                || visibility.equals(HIDDEN)
                || visibility.equals(AFTER_DUE_DATE)
                || visibility.equals(AFTER_PUBLISHED);
    }
}
