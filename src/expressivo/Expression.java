/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import expressivo.parser.ExpressionLexer;
import expressivo.parser.ExpressionMainVisitor;
import expressivo.parser.ExpressionParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTree;
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
    //                + Variable(id:String)
    //                + Addition(left:Expression, right:Expression)
    //                + Multiplication(left:Expression, right:Expression)
    /**
     * Parse an expression.
     * @param input expression to parse, as defined in the PS3 handout.
     * @return expression AST for the input
     * @throws IllegalArgumentException if the expression is invalid
     */
    // TODO: 'human-readable' error messages
    public static Expression parse(String input) {
        assert input != null && input != "";
        try {
            CharStream inputStream = CharStreams.fromString(input);
            ExpressionLexer lexer = new ExpressionLexer(inputStream);
            lexer.reportErrorsAsExceptions();
            
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ExpressionParser parser = new ExpressionParser(tokens);
            parser.reportErrorsAsExceptions();
            
            parser.setBuildParseTree(true);
            ParseTree parseTree = parser.root();
            
            ExpressionMainVisitor exprVisitor = new ExpressionMainVisitor();
            Expression expr = exprVisitor.visit(parseTree);
            
            return expr;
        } catch (ParseCancellationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
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
     * Checks if this expression is an addition
     * @return true if this expression is an addition
     */
    public boolean isAddition();
    /**
     * Produces an expression with the derivative of this expression 
     * with respect to an input variable
     * @param variable case-sensitive letters-only nonempty string used to differentiate.
     * @return the derivative of this expression with respect
     *         to variable. Must be a valid expression equal to the derivative, 
     *         but doesn't need to be in simplest or canonical form.
     */
    public Expression differentiate(String variable);
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
}
