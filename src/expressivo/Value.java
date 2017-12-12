package expressivo;

/**
 * An immutable type representing a non-negative number in an expression
 */
public class Value implements Expression {
    private final String num;
    
    // Abstraction Function:
    //   represents a nonnegative decimal number as an expression, 
    //   which consists of digits and an optional decimal point
    //   
    // Representation Invariant:
    //   num is a string sequence of digits with an optional
    //   single dot to separate two subsequences.
    //   The dot must appear either between two digits or 
    //   at the end of the sequence
    //
    // Safety From Exposure
    //   - num is a private and immutable reference
    //   - num references a String object which is 
    //     guaranteed immutable
    
    private void checkRep() {
        assert num != null && num != "";
        assert !num.contains("-");
        assert num.matches("\\d+(\\.\\d*)?");
    }
    public Value(String num) {
        this.num = num;
        checkRep();
    }
    @Override public Expression add(Expression e) {
        return new Addition(this, e);
    }
    @Override public Expression multiply(Expression e) {
        return new Multiplication(this, e);
    } 
    @Override public boolean isAddition() {
        return false;
    }
    @Override public String toString() {
        return this.num;
    }
    @Override public boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (!(thatObject instanceof Value)) {
            return false;
        }
        Value thatValue = (Value) thatObject;
        Double thatNum = Double.valueOf(thatValue.toString());
        Double thisNum = Double.valueOf(this.num);
        
        return thisNum.equals(thatNum);
    }
    @Override public int hashCode() {
        final int prime = 37;
        int result = 1;
        double numDouble = Double.parseDouble(this.num);
        long numLong = Double.doubleToLongBits(numDouble);
        int numHash = (int) (numLong ^ (numLong >>> 32));
        
        result = prime*result + numHash;
        return result;
    }
}
