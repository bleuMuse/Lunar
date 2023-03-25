package com.bleumuse.lunar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// nice
import static com.bleumuse.lunar.TokenType.*;

public class Scanner {

    private static final Map<String, TokenType> keywords;

    // initialize the keywords map with reserved keywords as strings and corresponding TokenType values
    static { // executed before any instance of the Scanner class is created, initialization is done once
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

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
            start = current; // start is updated to the index of the last character of the previous lexeme
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
                    // continue to consume the characters of the comment until newline is reached or EOF, effectively ignoring the comment
                    while (peek() != '\n' && !isAtEnd()) // one character of lookahead, does not consume the peeked character here
                        // now consume the character and advance the current pointer
                        advance(); 
                // this matches a block-comment, no token to add
                } else if (match('*')) { 
                    // entering block-comment handling logic 
                    while (peek() != '*' && peekNext() != '/' && !isAtEnd()) // one and two-character lookahead
                        advance();
                // current is updated to point to the first character following the comment
                } else {
                    // it is the division operator
                    addToken(SLASH);
                }
                break;
            // handle newlines and other spaces
            case ' ': // ignore
            case '\r': // ignore
            case '\t': // ignore
                // Ignore whitespace
                break;

            case '\n':
                line++;
                break;
            // handle string literals
            case '"':
                string(); // call the string() helper method
                break; // first character to match string is the opening quotation (")
            default:
                // handle number literals -- validate character as a decimal digit
                if (isDigit(c)) {
                    // consume the remaining number literal and create its token
                    number();
                // handle identifiers of the form [a-zA-Z_][a-zA-Z_0-9]*
                } else if (isAlpha(c)) {
                    // consume the sequence of alphanumeric characters and create its token
                    // as an enums type of keywords (reserved) or identifier
                    identifier();
                // handle unrecognized characters
                } else {
                    Lunar.error(line, "Unexpected character.");
                }
                break;
        }
    }

    /**
     * Consumes a sequence of alphanumeric characters from the input source, creating a new
     * token with the type IDENTIFIER and adding it to the token list.
     */
    private void identifier() {
        // while the current character is alphanumeric, consume it and increment the current pointer one position forward
        while (isAlphaNumeric(peek())) advance();

        // store the current lexeme defined by the boundaries of the start and current pointers 
        String text = source.substring(start, current);
        // retrieve the corresponding TokenType value of the lexeme if the key exists in the hashmap 'keywords' 
        TokenType type = keywords.get(text);
        // set the default TokenType to IDENTIFIER if no match for a reserved keyword was found
        if (type == null) type = IDENTIFIER;
        // create new Token as one of the reserved keyword TokenTypes or the IDENTIFIER TokenType, does not require a literal value
        addToken(type);
    }

    /**
     * Is called when a valid decimal digit has been detected and continues to
     * read the digits until the next adjacent character is not a digit or a decimal
     * followed by a digit. Creates a new NUMBER Token by passing the double literal 
     * as one of the arguments to addToken() call.
     */
    private void number() {
        // continue consuming characters of the whole number part if the current character is a valid decimal digit
        while (isDigit(peek()))
            advance();

        // Look for a fractional part, must have at least one digit following the "."
        if (peek() == '.' && isDigit(peekNext())) { // two-character lookahead, decimal with no trailing number is not allowed
            // Consume the "."
            advance();

            // continue consuming characters of the fractional part if the current character is a valid decimal digit
            while (isDigit(peek()))
                advance();
        }

        // parses the string literal to a double literal, creates a new NUMBER Token
        // type using the parsed double literal and appends it to this object's tokens list
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    /**
     * Is called when the start of a string has been detected and continues to
     * read the characters of the string until a terminating closing quotation is found.
     * Otherwise an unterminated string will be reported to the console's error stream.
     * Uses the string literal value and creates a new Token as a STRING Token type
     * and appends the newly created Token to this object's tokens field.
     */
    private void string() {
        // continue to peek until a closing quotation is returned, consume all
        // leading characters up to the closing quotation
        while (peek() != '"' && !isAtEnd()) {
            // support for multiline strings
            if (peek() == '\n')
                line++; // increment the line number count when a newline character is scanned
            advance(); // consume the character and move the current pointer forward
        }

        // print error message to error stream if EOF is reached, indicating that
        // no more input to read, string is not properly closed with closing quotations
        if (isAtEnd()) {
            Lunar.error(line, "Unterminated string.");
            return;
        }

        // Consume the closing quotation (")
        advance();

        // Trim the surrounding quotes
        String value = source.substring(start + 1, current - 1); // stores the string literal
        // create a new STRING Token using the STRING token type identifier and the string literal 
        // value and add it to this object's tokens list
        addToken(STRING, value); 
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
     * @return The null character '\0' if current is out of bounds and isAtEnd()
     *         returns true, otherwise returns the currently examined character
     *         without consuming it
     */
    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    /**
     * Provides functionality to view the character from the source string 
     * two characters ahead without consuming it.
     * @return the character two positions forward from the source string, else return '\0'
     */
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    } 

    /**
     * Determines whether the specified character is an alphabetic character or an underscore.
     * An alphabetic character is defined as any uppercase or lowercase letter between 'a' and 'z',
     * @param c character to check if it is alphabetical letter, ignoring case or an underscore
     * @return true if specified character matches one of [a-zA-Z-], false otherwise
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
    }
    
    /**
     * Determines whether the specified character is an alphanumeric character, which includes
     * uppercase and lowercase letters between 'a' and 'z' or 'A' and 'Z' or '-', as well as numeric
     * characters between '0' and '9'.
     * @param c the character to check
     * @return true if the character is alphanumeric, false otherwise
     */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * Determines if the character specified is a valid decimal digit by comparing
     * the character against the range of ASCII values between '0' and '9'. 
     * Returns true if the character is within the range (inclusive), returns false otherwise.
     * @param c The character to be checked if the character is a valid digit
     * @return true if character is a decimal digit, false otherwise
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9'; // ASCII 48 to 57
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
     * @param literal The Object literal representation of the token produced by the lexer
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}