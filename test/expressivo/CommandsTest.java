/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //   Partitions for differentiate(expression, variable) -> derivative:
    //    expression: contains one variable,
    //              multiple variables,
    //              one operator,
    //              multiple operators.
    //     where operators are addition and multiplication
    //     include expressions haaving groups
    //    variable: exists in expression,
    //              doesn't exist in expression
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    
    // TODO tests for Commands.differentiate() and Commands.simplify()
    // Tests for Command.differentiate()
    @Test
    // covers one operator, addition, 
    //        multiple variables,
    //        variable exists in expression
    public void testDifferentiate_Addition() {
        String inputExpr = "x + y + x";
        Expression expectedExpr = Expression.parse(inputExpr).differentiate("x");
        String expectedString = expectedExpr.toString();
        String outputExpr = Commands.differentiate(inputExpr, "x");
        
        assertNotEquals("Expected non-null string",
                null, outputExpr);
        assertNotEquals("Expected non-empty string", 
                "", outputExpr);
        assertEquals("Expected derived expression",
                expectedString, outputExpr);
        assertEquals("Expected a valid expression", 
                expectedExpr, Expression.parse(outputExpr));
    }    
    @Test
    // covers one operator, addition, 
    //        multiple variables,
    //        variable doesnt exist in expression
    public void testDifferentiate_AddVarNotExist() {
        String inputExpr = "x + y + x";
        Expression expectedExpr = Expression.parse(inputExpr).differentiate("foo");
        String expectedString = expectedExpr.toString();
        String outputExpr = Commands.differentiate(inputExpr, "foo");
        
        assertNotEquals("Expected non-null string",
                null, outputExpr);
        assertNotEquals("Expected non-empty string", 
                "", outputExpr);
        assertEquals("Expected derived expression",
                expectedString, outputExpr);
        assertEquals("Expected a valid expression", 
                expectedExpr, Expression.parse(outputExpr));
    } 
    @Test
    // covers multiple operators, 
    //        multiple variables,
    //        variable exists in expression
    public void testDifferentiate_AddMult() {
        String inputExpr = "x*y + x";
        Expression expectedExpr = Expression.parse(inputExpr).differentiate("x");
        String expectedString = expectedExpr.toString();
        String outputExpr = Commands.differentiate(inputExpr, "x");
        
        assertNotEquals("Expected non-null string",
                null, outputExpr);
        assertNotEquals("Expected non-empty string", 
                "", outputExpr);
        assertEquals("Expected derived expression",
                expectedString, outputExpr);
        assertEquals("Expected a valid expression", 
                expectedExpr, Expression.parse(outputExpr));
    }
    @Test
    // covers one operator, multiplication, 
    //        multiple variables,
    //        variable exists in expression
    public void testDifferentiate_Mult() {
        String inputExpr = "x * (x * y)";
        Expression expectedExpr = Expression.parse(inputExpr).differentiate("x");
        String expectedString = expectedExpr.toString();
        String outputExpr = Commands.differentiate(inputExpr, "x");
        
        assertNotEquals("Expected non-null string",
                null, outputExpr);
        assertNotEquals("Expected non-empty string", 
                "", outputExpr);
        assertEquals("Expected derived expression",
                expectedString, outputExpr);
        assertEquals("Expected a valid expression", 
                expectedExpr, Expression.parse(outputExpr));
    } 
    @Test
    // covers one operator, multiplication, 
    //        multiple variables,
    //        variable doesnt exist in expression
    public void testDifferentiate_MultVarNotExist() {
        String inputExpr = "x*(y + x)";
        Expression expectedExpr = Expression.parse(inputExpr).differentiate("foo");
        String expectedString = expectedExpr.toString();
        String outputExpr = Commands.differentiate(inputExpr, "foo");
        
        assertNotEquals("Expected non-null string",
                null, outputExpr);
        assertNotEquals("Expected non-empty string", 
                "", outputExpr);
        assertEquals("Expected derived expression",
                expectedString, outputExpr);
        assertEquals("Expected a valid expression", 
                expectedExpr, Expression.parse(outputExpr));
    } 
    @Test
    // covers multiple operators, 
    //        multiple variables,
    //        variable exists in expression,
    //        grouping
    public void testDifferentiate_MultAdd() {
        String inputExpr = "x*(y + x)";
        Expression expectedExpr = Expression.parse(inputExpr).differentiate("x");
        String expectedString = expectedExpr.toString();
        String outputExpr = Commands.differentiate(inputExpr, "x");
        
        assertNotEquals("Expected non-null string",
                null, outputExpr);
        assertNotEquals("Expected non-empty string", 
                "", outputExpr);
        assertEquals("Expected derived expression",
                expectedString, outputExpr);
        assertEquals("Expected a valid expression", 
                expectedExpr, Expression.parse(outputExpr));
    }
}
