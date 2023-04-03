package com.bleumuse.lunar;

import com.bleumuse.lunar.Expr.*;

/**
 * The Visitor interface defines the set of abstract methods
 * to perform operations for each of the grammar structures. Additional
 * implementing Visitor subclasses must have its behavior declared here.
 */
interface Visitor<R> {
    R visitBinaryExpr(Binary expr);
    R visitGroupingExpr(Grouping expr);
    R visitLiteralExpr(Literal expr);
    R visitUnaryExpr(Unary expr);
}