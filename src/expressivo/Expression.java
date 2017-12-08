/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import java.util.List;
/**
 * An immutable data type representing a polynomial expression of:
 *   + and *
 *   nonnegative integers and floating-point numbers
 *   variables (case-sensitive nonempty strings of letters)
 * 
 * <p>PS3 instructions: this is a required ADT interface.
 * You MUST NOT change its name or package or the names or type signatures of existing methods.
 * You may, however, add additional methods, or strengthen the specs of existing methods.
 * Declare concrete variants of Expression in their own Java source files.
 */
public interface Expression {
    
    // Datatype definition:
    //   Expression = Value(num:String)
    //                + Variable(name:String)
    //                + Addition(left:Expression, right:Expression)
    //                + Multiplication(left:Expression, right:Expression)
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    public static Expression parse(String input) {
        throw new RuntimeException("unimplemented");
    }
    /**
     * Returns the sum expression of this and another expression
     * @param e a non-null expression
     * @return expression AST of the sum of this and e
     */
    public Expression add(Expression e);
    /**
     * Returns the product expression of this and another expression
     * @param e a non-null Expression
     * @return the product of multiplying this
     *         by e 
     */
    public Expression multiply(Expression e);
    /**
     * Returns the two subexpressions of a binary operator
     * Addition and Multiplication expressions
     * are treated as binary operators:
     *      operand operator operand
     * with the operands being expressions. For example:
     *      x + x * y, is an addition having operands
     *      x and x * y.
     * or:
     *      (x + x) * y, is a multiplication having
     *      x + x and y as its operands
     * @return a doubleton list of the two subexpression
     *         operands in this expression in the order they
     *         appear
     */
    public List<Expression> getSubExpr();
    /**
     * Checks if this expression is an addition
     * @return true if this expression is an addition
     */
    public boolean isAddition();
    /**
     * @return a parsable representation of this expression, such that
     * for all e:Expression, e.equals(Expression.parse(e.toString())).
     */    
    @Override public String toString();
    /**
     * @param thatObject any object
     * @return true if and only if this and thatObject are structurally-equal
     * Expressions, as defined in the PS3 handout.
     */
    @Override
    public boolean equals(Object thatObject);
    
    /**
     * @return hash code value consistent with the equals() definition of structural
     * equality, such that for all e1,e2:Expression,
     *     e1.equals(e2) implies e1.hashCode() == e2.hashCode()
     */
    @Override
    public int hashCode();
    
    // TODO more instance methods
    
}
