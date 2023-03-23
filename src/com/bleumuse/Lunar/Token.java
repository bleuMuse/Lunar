package com.bleumuse.lunar;


/**
 * A class representing a single token in Lunar
 */
public class Token {
    final TokenType type;   // the type representing the token
    final String lexeme;    // the individual characters which comprises it
    final Object literal;   // a literal value of the token, if applies
    final int line;         // the line where the token appears


    /**
     * Constructs a new Token object with the given type, lexeme, literal, and line values
     * @param type The type of the token
     * @param lexeme The actual characters that make up the token
     * @param literal The literal value of the token
     * @param line The line number where the token appears in the source code
     */
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }


    /**
     * Returns a String representation of the Token object
     * The format of the String is as follows: "type lexeme literal".
     * For example, a token representing the number 42 might have a toString() value of "NUMBER 42 42".
     * @return A String representation of the Token object.
     */
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}