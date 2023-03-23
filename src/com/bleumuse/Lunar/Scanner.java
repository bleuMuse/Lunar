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
     * 
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
     * Scans for single characters and adds its corresponding token type to this
     * object's tokens list.
     * Prints an error message if an unrecognized character is detected in the
     * stream. The erroneous
     * character is still consumed by advance() in this case and moves along to the
     * next character.
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
            // check for two character operations
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            // handle operation for division
            case '/':
                // this matches a comment, no token to add
                if (match('/')) { // entering comment handling logic (peeked character is a '/')
                    // continue to consume the characters of the comment until '\n' is reached or
                    // EOF, effectively ignoring the comment
                    while (peek() != '\n' && !isAtEnd()) // one character of lookahead, does not consume the peeked character here
                        advance(); // now consume the character and advance the current pointer
                // current is updated to point to the first character following the comment
                } else {
                    // it is the division operator
                    addToken(SLASH);
                }
                break;
            // handle newlines and other spaces
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                break;

            case '\n':
                line++;
                break;
            default:
                Lunar.error(line, "Unexpected character.");
                break;
        }
    }

    /**
     * Examines the current character pointed to by the counter current
     * and returns the boolean true if it is equal to the character specified
     * and consumes the character if matched, returns false otherwise.
     * 
     * @param expected The character to match
     * @return true if the current character does not equal argument specified
     */
    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt(current) != expected)
            return false;

        current++;
        return true;
    }

    /**
     * Provides a one-character lookahead in the input stream without consuming it.
     * @return The null character '\0' if current is out of bounds and isAtEnd() returns true,
     * otherwise returns the currently examined character without consuming it
     */
    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    /**
     * Checks if the scanner has reached EOF, continues scanning
     * if EOF has not been reached.
     * 
     * @return true if EOF is reached and no more characters left to read, false
     *         otherwise
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * Consumes and returns the char at the current index of the source string
     * and increments the index by one position afterwards.
     * 
     * @return The char from the raw string source code currently being examined
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * Calls the overloaded addToken() method with the passed
     * token type and an additional null argument for the Token object literal field
     * 
     * @param type The type of token produced by the lexer
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Constructs a Token Object using the data from the currently
     * examined string of text and adds the constructed Token to this Object's
     * tokens list.
     * 
     * @param type    The type of token produced by the lexer
     * @param literal The Object literal representation of the token produced by the
     *                lexer
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}