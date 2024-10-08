package com.github.dscpsyl.jgrade2;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.github.dscpsyl.jgrade2.gradescope.GradescopeJsonFormatter;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;


/**
 * The class with the main entry point for JGrade. The jar file (if run as an
 * executable) will enter this main method. The main program takes a class that
 * is annotated with {@link Grade} methods and invokes all of those methods
 * passing in a single instance of a {@link Grader}.  It requires one argument,
 * a <code>-c</code> flag and the name of the class containing annotated
 * methods. It can produce output as specified by other options like a format
 * option, and an option to specify where the output should be written. If no
 * format is specified, the default is the Gradescope JSON, if no output file
 * is specified, output will just be written to standard out. The
 * <code>--no-output</code> flag can be used to not produce any output.
 *
 * The help/usage message is the following:
 * <code> <br>
 *    usage: jgrade<br>
 *      -c,--classname arg            the class containing annotated methods to grade<br>
 *      -f,--format output-format     specify output, one of 'json' (default) or 'txt'<br>
 *      -h,--help<br>
 *         --no-output                don't produce any output (if user overriding)<br>
 *      -o destination                save output to another file (if not specified,
 *                                    prints to standard out)<br>
 *         --pretty-print             pretty-print output (when format is json)<br>
 *      -v,--version<br>
 * </code>
 */
public final class JGrade2 {

    private static final String VERSION = "2.0.0-a2";

    private static final String CLASS_OPT = "classname";
    private static final String HELP_OPT = "help";
    private static final String VERSION_OPT = "version";
    private static final String NO_OUTPUT_OPT = "no-output";
    private static final String OUTPUT_OPT = "o";
    private static final String DEST_ARG = "destination";
    private static final String FORMAT_OPT = "format";
    private static final String FORMAT_ARG = "output-format";
    private static final String PP_OPT = "pretty-print";
    private static final String JSON_VAL = "json";
    private static final String TXT_VAL = "txt";
    private static final String DEFAULT_FORMAT = JSON_VAL;


    private static GradescopeJsonFormatter formatter;

    /**
     * Private constructor to prevent instantiation.
     */
    private JGrade2() { }


    /**
     * A fatal error occurred. Prints the message and stack trace and exits.
     * @param msg The message to print
     * @param e The exception to print
     */
    private static void fatal(String msg, Exception e) {
        msg = "[FATAL] A fatal jGrade error occurred: \n" + msg;
        throw new RuntimeException(msg, e);
    }

    /**
     * Prints the usage message.
     */
    private static void usage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(80);
        formatter.printHelp("jgrade", getOptions());
    }

    /**
     * Outputs the result of the grading if set by the user
     * @param grader The grader to output.
     * @param line The command line arguments.
     */
    private static void outputResult(Grader grader, CommandLine line) {
        if (line.hasOption(NO_OUTPUT_OPT)) {
            return;
        }

        PrintStream out = System.out;
        if (line.hasOption(OUTPUT_OPT)) {
            try {
                out = new PrintStream(line.getOptionValue(OUTPUT_OPT));
            } catch (FileNotFoundException e) {
                fatal("error printing output to file", e);
            }
        }

        if (formatter != null) {
            out.println(formatter.format(grader));
        }
    }

    /**
     * Initializes the grader based on the command line arguments.
     * @param line The command line arguments.
     * @return The initialized grader.
     */
    private static Grader initGrader(CommandLine line) {
        formatter = null;

        Grader grader = new Grader();
        if (line.hasOption(FORMAT_OPT) && !line.getOptionValue(FORMAT_OPT).equals(DEFAULT_FORMAT)) {
            String val = line.getOptionValue(FORMAT_OPT);
            switch (val) {
                case TXT_VAL:
                    throw new UnsupportedOperationException("have not implemented textual output");
                default:
                    throw new IllegalArgumentException("unrecognized format value " + val);
            }
        } else if (!line.hasOption(NO_OUTPUT_OPT)) {
            formatter = new GradescopeJsonFormatter();
        }

        if (line.hasOption(PP_OPT)) {
            if (formatter == null) {
                throw new IllegalArgumentException("pretty-print without json formatting");
            }
            formatter.setPrettyPrint(2);
        }

        return grader;
    }

    /**
     * Grades the class.
     * @param grader The grader to use.
     * @param c The class to grade.
     */
    private static void grade(Grader grader, Class<?> c) {
        Object o = instantiateClass(c);
        for (Method m : ReflectGrade.graderMethods(c)) {
            try {
                m.invoke(o, grader);
            } catch (IllegalAccessException | InvocationTargetException e) {
                System.err.printf("failed invoking method %s\n", m.getName());
                e.printStackTrace(System.err);
            }
        }
    }

    /**
     * Instantiates the class to grade.
     * @param c The class to instantiate.
     * @return The instantiated class.
     */
    private static Object instantiateClass(Class<?> c) {
        try {
            return c.getConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException
                | NoSuchMethodException | InvocationTargetException e) {
            fatal("could not invoke constructor of " + c.getName(), e);
            throw new InternalError("instantiateClass::unreachable statement - system should have exited");
        }
    }

    /**
     * Gets the class to grade.
     * @param className The name of the class to grade.
     * @return The class to grade.
     */
    private static Class<?> getClassToGrade(String className) {
        try {
            return ReflectGrade.load(className);
        } catch (MalformedURLException | ClassNotFoundException e) {
            fatal("could not locate class " + className, e);
            throw new InternalError("getClassToGrade::unreachable statement - system should have exited");
        }
    }

    /**
     * Gets the options for the command line.
     * @return The options for the command line.
     */
    private static Options getOptions() {
        Options options = new Options();
        options.addOption(Option.builder("f").longOpt(FORMAT_OPT)
                .desc("specify output, one of \'json\' (default) or \'txt\'")
                .hasArg(true)
                .argName(FORMAT_ARG)
                .build());
        options.addOption(Option.builder().longOpt(PP_OPT)
                .desc("pretty-print output (when format is json)")
                .build());
        options.addOption(Option.builder().longOpt(NO_OUTPUT_OPT)
                .desc("don't produce any output (if user overriding)")
                .build());
        options.addOption(Option.builder(OUTPUT_OPT)
                .desc("save output to another file (if not specified, prints to standard out)")
                .hasArg(true)
                .argName(DEST_ARG)
                .build());
        options.addOption(Option.builder("c").longOpt(CLASS_OPT)
                .desc("the class containing annotated methods to grade")
                .hasArg()
                .build());
        options.addOption(Option.builder("h").longOpt(HELP_OPT).build());
        options.addOption(Option.builder("v").longOpt(VERSION_OPT).build());
        return options;
    }

    /**
     * Reads the command line arguments.
     * @param args The command line arguments.
     * @return The parsed command line arguments.
     */
    private static CommandLine readCommandLine(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(getOptions(), args, false);
    }

    /**
     * Main entry point. See usage or run with <code>--help</code>
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        CommandLine line = null;
        try {
            line = readCommandLine(args);
        } catch (ParseException e) {
            fatal("could not parse command line", e);
        }

        assert line != null;

        if (args.length == 0 || line.hasOption(HELP_OPT)) {
            usage();
        } else if (line.hasOption(VERSION_OPT)) {
            System.out.println(VERSION);
        } else if (!line.hasOption(CLASS_OPT)) {
            fatal("missing required class flag", new ParseException("missing required class flag"));
        } else {
            Grader grader = initGrader(line);
            Class<?> c = getClassToGrade(line.getOptionValue(CLASS_OPT));
            grade(grader, c);
            outputResult(grader, line);
        }
    }
}
