package com.bleumuse.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * A tool for generating abstract syntax tree (AST) classes based on a list of types.
 * The tool generates a single output file that contains all of the AST classes.
 */
public class GenerateAst {
    /**
     * The entry point for the tool.
     * @param args An array containing a single argument: the output directory for the generated file.
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        // Generate the AST classes for the specified types
        defineAst(outputDir, "Expr", Arrays.asList(
           "Binary   : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Unary    : Token operator, Expr right"));
    }

    /**
     * Generates an AST class for each type in the list and writes the output to a file.
     * @param outputDir The directory where the output file should be written.
     * @param baseName The base name for the generated classes.
     * @param types A list of strings, where each string represents a type and its fields.
     */
    private static void defineAst(
            String outputDir, String baseName, List<String> types) 
            throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        // write the package and import statements
        writer.println("package com.bleumuse.lunar;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        // write the abstract base class definition
        writer.println("abstract class " + baseName + " {");

        // Generate an AST class for each type
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("}");
        writer.close();
    }

    /**
     * Generates the code for an AST class and writes it to the output file.
     * @param writer A PrintWriter object that is used to write to the output file.
     * @param baseName The base name for the generated classes.
     * @param className The name of the AST class being generated.
     * @param fieldList A string containing a comma-separated list of field declarations for the AST class.
     */
    private static void defineType(
            PrintWriter writer, String baseName,
            String className, String fieldList) {
        // Write the class definition
        writer.println("  static class " + className + " extends " +
                baseName + " {");

        // Write the constructor
        writer.println("    " + className + "(" + fieldList + ") {");

        // Store parameters in fields
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // Write the field definitions
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println("  }");
    }
}