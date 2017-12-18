/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Tests for the static methods of Commands.
 */
public class CommandsTest {

    // Testing strategy
    //   Partitions for differentiate: Expression x String -> Expression:
    //    expression: contains one variable,
    //              multiple variables,
    //              one operator,
    //              multiple operators.
    //     where operators are addition and multiplication
    //     include expressions having groups
    //    variable: exists in expression,
    //              doesn't exist in expression
    //
    //   Partitions for simplify: Expression x Map -> Expression
    //     expression: contains no variable in map,
    //              contains one variable in map,
    //              contains all the variables in map,
    //              contains multiple variables in map
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
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
    
    // Tests for Commands.simplify()
    @Test
    // covers expression contains no variable in map
    public void testSimplify_NotExist() {
        String expr = "m*x + c";
        Map<String, Double> env = new HashMap<>();
        
        env.put("PI", 3.142);
        env.put("radius", 12.0);
        String actual = Commands.simplify(expr, env);
        String expected = Expression.parse(expr).toString();
        
        assertNotNull("Expected non-null string expression", actual);
        assertNotEquals("Expected non-empty string", "", actual);
        assertEquals("Expected unchanged string", expected, actual);
    }
    @Test
    // covers one variable in expression and map
    public void testSimplify_OneVar() {
        String expr = "PI*diameter";
        Map<String, Double> env = new HashMap<>();
        
        env.put("PI", 3.142);
        env.put("radius", 12.0); 
        String actual = Commands.simplify(expr, env);
        String expected = Expression.parse("3.142*diameter").toString();
        
        assertNotNull("Expected non-null string expression", actual);
        assertNotEquals("Expected non-empty string", "", actual);
        assertEquals("Expected variable substituted", expected, actual);
    }
    @Test
    // covers all variables in expression are in map
    public void testSimplify_AllVars() {
        String expr = "PI * (radius + radius)";
        Map<String, Double> env = new HashMap<>();
        
        env.put("PI", 3.142);
        env.put("radius", 12.0); 
        String actual = Commands.simplify(expr, env);
        String expected = String.valueOf(3.142 * (12.0 + 12.0)); 
        
        assertNotNull("Expected non-null string expression", actual);
        assertNotEquals("Expected non-empty string", "", actual);
        assertEquals("Expected all variables substituted and simplified", 
                expected, actual);
    }
    @Test
    // covers multiple variables in both expression and map
    public void testSimplify_MultipleVars() {
        String expr = "0.5*length*width + PI*(length*0.5)";
        Map<String, Double> env = new HashMap<>();
        
        env.put("PI", 3.142);
        env.put("length", 4.0);
        env.put("width", 2.0); 
        String actual = Commands.simplify(expr, env);
        String expected = Expression.parse("(0.5*4*2) + (3.142*4*0.5)").toString();  
        
        assertNotNull("Expected non-null string expression", actual);
        assertNotEquals("Expected non-empty string", "", actual);
        assertEquals("Expected variables substituted and simplified", 
                expected, actual);
    }
}
