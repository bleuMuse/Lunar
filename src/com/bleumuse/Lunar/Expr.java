package com.bleumuse.lunar;

import java.util.List;

/**
 * Defines the context-free grammars of the Lunar language. Each expression is
 * composed of a set of productions which include terminals and non-terminals. A terminal 
 * represents an individual lexeme or token and no further. A non-terminal recurses back
 * and leads to another set of rules. The resulting derivation or sequence of string from 
 * the set of productions is terminated with a semicolon.
 * 
 * Expr
 *  \_ Binary
 *  \_ Grouping
 *  \_ Literl
 *  \_ Unary
 */
abstract class Expr {

  // each implementing visitable class must define
  // functionality for the accept() method
  abstract <R> R accept(Visitor<R> visitor);

  /**
   * Binary expr is a recursive structure and may be used to generate 
   * a new Expression of any of the defined types. The Token operator 
   * selects for a valid token and splits the two operand Expressions
   * on the LHS and RHS.
   */
  static class Binary extends Expr {
    // Binary expr constructor 
    Binary(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    /**
     * Performs task on Binary Expression, passing itself 
     * or its own instance as the "visitor"
     */
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinaryExpr(this);
    }

    final Expr left;
    final Token operator;
    final Expr right;
  }

  /**
   * The Grouping grammar production is a recursive production and wraps 
   * one or more parenthesis around a single Expression.
   */
  static class Grouping extends Expr {
    // Grouping expr constructor 
    Grouping(Expr expression) {
      this.expression = expression;
    }

    /**
     * Performs task on Grouping Expression, passing itself 
     * or its own instance as the "visitor"
     */
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitGroupingExpr(this);
    }

    final Expr expression;
  }

  /**
   * The Literal is a terminal production and maps to a single
   * symbol. Literals includes NUMBER, STRING, (whose text representation
   * is subject to vary), boolean TRUE, FALSE, and NIL.
   */
  static class Literal extends Expr {
    // Literal constructor 
    Literal(Object value) {
      this.value = value;
    }

    /**
     * Performs task on Literal, passing itself 
     * or its own instance as the "visitor"
     */
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteralExpr(this);
    }

    final Object value;
  }

  /**
   * The Unary grammar structure maps to a single Token operator
   * on LHS and is followed by an Expression on the RHS.
   */
  static class Unary extends Expr {
    // Unary expr constructor
    Unary(Token operator, Expr right) {
      this.operator = operator;
      this.right = right;
    }

    /**
     * Performs task on Unary Expression, passing itself 
     * or its own instance as the "visitor"
     */
    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnaryExpr(this);
    }

    final Token operator;
    final Expr right;
  }
}
