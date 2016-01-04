package com.lilalinda.younicos;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.PrintStream;

public final class Main {

    private static void printUsage(PrintStream out, CmdLineParser parser) {
        out.println("usage: java -jar Younicos-<...>-jar-with-dependencies.jar [options...]");
        out.println("with");
        parser.printUsage(out);
    }

    public static void main(String[] args) {
        // parse arguments
        YounicosOptions options = new YounicosOptions();
        CmdLineParser parser = new CmdLineParser(options);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            printUsage(System.err, parser);
            return;
        }

        if (options.displayHelp) {
            printUsage(System.out, parser);
            return;
        }

        // TODO: run validation
        System.out.println("Running validation");
    }

    static class YounicosOptions {
        @Option(name="-h",usage="display usage and exit")
        boolean displayHelp = false;
    }
}
