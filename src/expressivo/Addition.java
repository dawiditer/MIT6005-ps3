package expressivo;

/**
 * An immutable type representing an addition expression
 */
public class Addition implements Expression {
    private final Expression left;
    private final Expression right;

    // Abstraction Function
    //   represents an addition expression made up of
    //   two subexpressions
    //
    // Representation Invariant
    //   The left and right are non-null immutable expressions
    //
    // Safety From Exposure
    //   - All fields are private and final
    //   - left and right are immutable
    //   - Addition shares its rep with other implementations
    //     but they do not modify it
    
    private void checkRep() {
        assert left != null;
        assert right != null;
    }
    public Addition(Expression left, Expression right) {
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
    @Override public boolean isAddition() {
        return true;
    }
    /**
     * Returns a string representation of this expression
     * A single space exists between each operand and 
     * the (+) operator symbol. The string rep is equivalent to:
     *            operand + operand
     * @return a parsable representation of this addition expression, 
     *         such that:
     *         for all a:Addition, a.equals(Expression.parse(a.toString())).
     */
    @Override public String toString() {
        return this.left.toString()
               + " + "
               + this.right.toString();
    }
    /**
     * Checks if an object is equal to this addition expression
     * Two expressions are equal if and only if: 
     *   - The expressions contain the same variables, numbers, and operators;
     *   - those variables, numbers, and operators are in the same order, read left-to-right;
     *   - and they are grouped in the same way.
     * Two addition objects are equal if having different groupings with 
     * the same mathematical meaning. For example, 
     *     (3 + 4) + 5 and 3 + (4 + 5) are equal.
     * @param thatObject Object to compare equality with
     * @return true if two addition expressions are equal
     */
    @Override public boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (!(thatObject instanceof Addition)) {
            return false;
        }
        Addition thatAdd = (Addition) thatObject;
        
        return this.toString().equals(thatAdd.toString());
    }
    @Override public int hashCode() {
        final int prime = 37;
        int result = 1;
        
        result = prime*result + this.left.hashCode();
        result = prime*result + this.right.hashCode();
        
        return result;
    }
}
