package expressivo;

import java.util.Arrays;
import java.util.List;
/**
 * An immutable type representing a multiplication expression
 */
public class Multiplication implements Expression{
    private final Expression left;
    private final Expression right;

    // Abstraction Function
    //   represents a multiplication expression made up of
    //   two subexpressions
    //
    // Representation Invariant
    //   The left and right are non-null immutable expressions
    //
    // Safety From Exposure
    //   - All fields are private and final
    //   - left and right are required to be immutable
    //   - Multiplication shares its rep with other implementations
    //     but they do not modify it
    
    private void checkRep() {
        assert left != null;
        assert right != null;
    }
    public Multiplication(Expression left, Expression right) {
        this.left = left;
        this.right = right;
        checkRep();
    }
    @Override public Expression add(Expression e) {
        return new Addition(this, e);
    }
    @Override public Expression multiply(Expression e) {
        return new Multiplication(this, e);
    }
    @Override public List<Expression> getSubExpr() {
        return Arrays.asList(this.left, this.right);
    }
    @Override public boolean isAddition() {
        return false;
    }
    /**
     * Returns a string representation of this expression
     * No spaces between each operand and the (*) operator symbol. 
     * The string rep is equivalent to:
     *            operand*operand
     * @return a parsable representation of this multiplication expression, 
     *         such that:
     *         for all m:Multiplication, m.equals(Expression.parse(m.toString())).
     */
    @Override public String toString() {
        String leftString = this.left.toString();
        String rightString = this.right.toString();
        if (this.left.isAddition()) {
           leftString = "(" + leftString + ")";
        }
        if (this.right.isAddition()) {
           rightString = "(" + rightString + ")";
        }
        
        return leftString + "*" + rightString;
    }    
    /**
     * Checks if an object is equal to this multiplication expression
     * Two expressions are equal if and only if: 
     *   - The expressions contain the same variables, numbers, and operators;
     *   - those variables, numbers, and operators are in the same order, read left-to-right;
     *   - and they are grouped in the same way.
     * @param thatObject Object to compare equality with
     * @return true if two multiplication expressions are equal
     */
    @Override public boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (!(thatObject instanceof Multiplication)) {
            return false;
        }
        Multiplication thatMult = (Multiplication) thatObject;
        
        return this.toString().equals(thatMult.toString());
    }
    @Override public int hashCode() {
        final int prime = 37;
        int result = 1;
        
        result = prime*result + this.left.hashCode();
        result = prime*result + this.right.hashCode();
        
        return result;
    }

}
