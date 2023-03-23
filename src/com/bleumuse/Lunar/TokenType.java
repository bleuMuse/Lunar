package com.bleumuse.lunar;


/**
 * Represents the different types of tokens that can be produced by the lexer.
 * Each token type corresponds to a specific character or sequence of characters
 * that are used in the language syntax.
 */
public enum TokenType {
    // Single-character tokens (Operations)
    LEFT_PAREN,     // ( Used to group expressions or define function parameters or arguments
    RIGHT_PAREN,    // ) Closes a grouping of expressions or a function call
    LEFT_BRACE,     // { Defines a block of code or a compound statement
    RIGHT_BRACE,    // } Closes a block of code or a compound statement
    COMMA,          // , Separates items in a list or arguments in a function call
    SEMICOLON,      // ; Indicates the end of a statement
    DOT,            // . ACCESSOR
    MINUS,          // - SUBTRACTION, NEGATION
    PLUS,           // + ADDITION
    SLASH,          // / DIVISION
    STAR,           // * MULTIPLICATION

    // One or two character tokens (Comparison Operators)
    BANG,           // !  
    BANG_EQUAL,     // !=
    EQUAL,          // = 
    EQUAL_EQUAL,    // ==
    GREATER,        // >
    GREATER_EQUAL,  // >=
    LESS,           // <
    LESS_EQUAL,     // <=

    // Literals (Type)
    IDENTIFIER,     // VARIABLE NAME
    STRING,         // STRING LITERAL
    NUMBER,         // NUMBER LITERAL

    // Keywords (Reserved)
    AND,            // LOGICAL AND
    OR,             // LOGICAL OR
    FALSE,          // BOOLEAN LITERAL FALSE
    TRUE,           // BOOLEAN LITERAL TRUE
    IF,             // IF STATEMENT
    ELSE,           // ELSE STATEMENT
    FOR,            // FOR-LOOP
    WHILE,          // WHILE-LOOP
    PRINT,          // PRINT STATEMENT
    RETURN,         // RETURN STATEMENT
    NIL,            // NULL LITERAL
    CLASS,          // CLASS DEFINITION
    FUN,            // FUNCTION DEFINITION
    SUPER,          // SUPERCLASS REFERENCE
    THIS,           // THIS OBJECT REFERENCE
    VAR,            // VARIABLE NAME
    
    EOF
}
