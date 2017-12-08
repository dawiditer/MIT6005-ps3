package expressivo;

import java.util.List;
/**
 * An immutable type representing a named variable in an expression
 * Variable names are case-sensitive
 */
public class Variable implements Expression {
    private final String id;
    
    // Abstraction Function
    //   represents a named variable as an expression
    //
    // Representation Invariant
    //   - id.length > 0
    //   - id.charAt(n) must be a letter, a-zA-Z 
    //     for all 0 < n < id.length
    //
    // Safety From Exposure
    //   - id is private and final
    //   - id is a string which is immutable
    //   - Variable shares its rep with other implementations
    //     but they do not mutate it
    
    private void checkRep() {
        assert id != null && id != "";
        assert id.length() > 0;
        assert id.matches("\\w+");
    }
    public Variable(String id) {
        this.id = id;
        checkRep();
    }
    @Override public Expression add(Expression e) {
        return new Addition(this, e);
    }
    @Override public Expression multiply(Expression e) {
        return new Multiplication(this, e);
    }
    @Override public List<Expression> getSubExpr() {
        throw new UnsupportedOperationException();
    }
    @Override public boolean isAddition() {
        return false;
    }
    @Override public String toString() {
        return this.id;
    }
    @Override public boolean equals(Object thatObject) {
        if (thatObject == this) {
            return true;
        }
        if (!(thatObject instanceof Variable)) {
            return false;
        }
        Variable thatVar = (Variable) thatObject;
        String thatId = thatVar.toString();
        
        return this.id.equals(thatId);
    }
    @Override public int hashCode() {
        return this.id.hashCode();
    }
}
