package com.bleumuse.lunar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// nice
import static com.bleumuse.lunar.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    /**
     * Populates this object's list containing only Token objects
     * until there are no further lexemes to read from.
     * @return This object's list of tokens 
     */
    private List<Token> scanTokens() {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme
            start = current;
            scanToken();
        }

        // appends one final EOF token once all tokens have been read
        this.tokens.add(new Token(EOF, "", null, line));
        return this.tokens;
    }

    /**
     * Scans for single characters and adds its corresponding token type to this object's tokens list.
     * Prints an error message if an unrecognized character is detected in the stream.
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            default:
                Lunar.error(line, "Unexpected character.");
                break;
        }
    }

    /**
     * Checks if the scanner has reached EOF, continues scanning
     * if EOF has not been reached.
     * @return true if EOF is reached and no more characters left to read, false otherwise
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * Consumes and returns the char at the current index of the source string 
     * and increments the index by one position afterwards.
     * @return The char from the raw string source code currently being examined
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * Calls the overloaded addToken() method with the passed
     * token type and an additional null argument for the Token object literal field
     * @param type The type of token produced by the lexer
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Constructs a Token Object using the data from the currently
     * examined string of text and adds the constructed Token to this Object's tokens list.
     * @param type The type of token produced by the lexer
     * @param literal The Object literal representation of the token produced by the lexer
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}