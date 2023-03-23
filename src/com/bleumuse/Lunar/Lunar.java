package com.bleumuse.lunar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lunar {
    static boolean hadError = false;
    public static void main(String[] args) throws IOException {
        // accepts one arg [filename] or none
        if (args.length > 1) {
            System.out.println("Usage: lunar [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt(); // runs the interactive prompt instead
        }
    }

    // reads the file from path
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        // runs the input through the core function
        run(new String(bytes, Charset.defaultCharset()));

        // indicate an error in the exit code
        if (hadError) System.exit(65);
    }

    // running the REPL, executes line by line
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");
            String line = reader.readLine();
            // checking for EOF condition or early termination from keyboard
            if (line == null)
                break;
            run(line);
            // resetting flag to continue the current session
            hadError = false;
        }
    }

    // runFile and runPrompt are wrapper functions for run
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        // printing the tokens
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    // raises an en error in the program
    static void error(int line, String message) {
        report(line, "", message);
    }

    // reports back a message and the location of where the error has been raised
    private static void report(int line, String where,
            String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
