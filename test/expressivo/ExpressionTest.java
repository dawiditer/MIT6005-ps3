/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
// TODO: include tests for Expression.parse() and variant.isAddition()
/**
 * Tests for the concrete variants of Expression
 */
public class ExpressionTest {
    // Testing Strategy for Expression.parse()
    //  Partition the input as follows:
    //   For operators + and * :
    //     one operator,
    //     multiple operators of the same type,
    //     multiple operators of different types.
    //   include inputs with grouping
    //
    // Testing Strategy for the Expression variants:
    //  Partition the input as follows:
    //   For all variants:
    //     Value, Variable, Addition, Multiplication
    //   Input partitions are:
    //    variant.add(otherVariant)
    //      this variant included as otherVariant  
    //    variant.multiply(otherVariant)
    //      this variant included as otherVariant
    //    variant.getSubExpr()
    //      - Addition
    //      - Multiplication
    //    variant.toString()
    //      include parse tests to make sure the string is parsable
    //    variant.equals(variant)
    //      - reflexive
    //      - symmetric
    //      - transitive
    //    variant.hashCode()
    //
    // Full Cartesian Coverage of partitions
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    //Tests for Expression.parse()
    @Test
    // covers single addition expression
    public void testParse_SingleAdd() {
        String input = "2 + x";
        Expression expected = 
                new Addition(new Value("2"), new Variable("x"));
        Expression wrongOrder = 
                new Addition(new Variable("x"), new Value("2"));
        Expression parsed = Expression.parse(input);
        
        assertNotEquals("Expected non-null expression", 
                null, parsed);
        assertNotEquals("Expected correct order of elements", 
                wrongOrder, parsed);
        assertEquals("Expected correct parse",
                expected, parsed);
    }
    @Test
    // covers multiple additions
    public void testParse_MultipleAdds() {
        String input = "(x + x + y)";
        Expression expected = new Addition(
                        new Addition(new Variable("x"), new Variable("x")),
                        new Variable("y"));
        Expression parsed = Expression.parse(input);
   
        assertNotEquals("Expected non-null expression", 
                null, parsed);
        assertEquals("Expected correct parse",
                expected, parsed);
    }
    @Test
    // covers single multiplication expression
    public void testParse_SingleMult() {
        String input = "x*y";
        Expression expected =
                new Multiplication(new Variable("x"), new Variable("y"));
        Expression wrongOrder =
                new Multiplication(new Variable("y"), new Variable("x"));
        Expression parsed = Expression.parse(input);
        
        assertNotEquals("Expected non-null expression", 
                null, parsed);
        assertNotEquals("Expected correct order of elements", 
                wrongOrder, parsed);
        assertEquals("Expected correct parse",
                expected, parsed);
    }
    @Test
    // covers multiple multiplication expressions
    public void testParse_MultipleMults() {
        String input = "x*2*x";
        Variable x = new Variable("x");
        Value num = new Value("2");
        Expression expected = 
                new Multiplication(
                        new Multiplication(x, num), x);
        Expression parsed = Expression.parse(input);
        
        assertNotEquals("Expected non-null expression", 
                null, parsed);
        assertEquals("Expected correct parse",
                expected, parsed);
    }
    @Test
    // covers addition, multiplication and grouping
    public void testParse_AddMult() {
        String input = "(x + (2.12*x))*y";
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Value num = new Value("2.12");
        Multiplication group1 =
                new Multiplication(num, x);
        Addition group2 =
                new Addition(x, group1);
        Expression expected = 
                new Multiplication(group2, y);
        Expression parsed = Expression.parse(input);
        
        assertNotEquals("Expected non-null expression", 
                null, parsed);
        assertEquals("Expected correct parse",
                expected, parsed);
    }
    
    // Tests for variant.add(otherVariant)
    @Test
    // covers value + value
    public void testAdd_Values() {
        // 1 + 1
        Value valueExpr = new Value("1");
        Expression addition = valueExpr.add(valueExpr);
        Expression expected = new Addition(valueExpr, valueExpr);
        
        assertNotEquals("Expected non-null", null, valueExpr);
        assertNotEquals("Expected non-null", null, addition);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected addition expression", 
                expected, addition);
    }
    @Test
    // covers variable + variable
    public void testAdd_Variables() {
        // x + x
        Variable varExpr = new Variable("x");        
        Expression expected = new Addition(varExpr, varExpr);
        Expression addition = varExpr.add(varExpr);
        
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected addition expression", 
                expected, addition);
    }
    @Test
    // covers variable + value
    public void testAdd_VariableValue() {
        // x + 2.2
        Variable varExpr = new Variable("x");
        Value valueExpr = new Value("2.2");
        
        Expression expected = new Addition(valueExpr, varExpr);
        Expression addition = valueExpr.add(varExpr);
        
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, valueExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected addition expression", 
                expected, addition);
    }
    @Test
    // covers sum + variable
    public void testAdd_SumVariable() {
        // (x + y) + y
        Variable varExpr = new Variable("x");
        Variable right = new Variable("y");
        Addition sumExpr = new Addition(varExpr, right);
        
        Expression expected = new Addition(sumExpr, varExpr);
        Expression addition = sumExpr.add(varExpr);
        
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, sumExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected addition expression", 
                expected, addition);        
    }
    @Test
    // covers variable + product
    public void testAdd_VariableProduct() {
        // x + (x*y)
        Variable varExpr = new Variable("x");
        Variable right = new Variable("y");
        Multiplication multExpr = new Multiplication(varExpr, right);
        
        Expression expected = new Addition(varExpr, multExpr);
        Expression addition = varExpr.add(multExpr);
        
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, multExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected addition expression", 
                expected, addition);
    }
    @Test
    // covers sum + sum
    public void testAdd_Sums() {
        // (x + 2) + (1 + y)
        Addition leftSumExpr = 
                new Addition(new Variable("x"), new Value("2"));
        Addition rightSumExpr = 
                new Addition(new Value("1"), new Variable("y"));
        
        Expression expected = new Addition(leftSumExpr, rightSumExpr);
        Expression addition = leftSumExpr.add(rightSumExpr);
        
        assertNotEquals("Expected non-null", null, leftSumExpr);
        assertNotEquals("Expected non-null", null, rightSumExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected addition expression", 
                expected, addition);
    }
    @Test
    // covers sum + product
    public void testAdd_SumProduct() {
        // (x + 2) + (x*y)
        Addition sumExpr = 
                new Addition(new Variable("x"), new Value("2"));
        Multiplication multExpr = 
                new Multiplication(new Variable("x"), new Variable("y"));
        
        Expression expected = new Addition(sumExpr, multExpr);
        Expression addition = sumExpr.add(multExpr);
        
        assertNotEquals("Expected non-null", null, multExpr);
        assertNotEquals("Expected non-null", null, sumExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected addition expression", 
                expected, addition);
    }
    @Test
    // covers product + product
    public void testAdd_Products() {
        // (2*x) + (x*y)
        Multiplication multExpr1 = 
                new Multiplication(new Variable("2"), new Variable("x"));
        Multiplication multExpr2 = 
                new Multiplication(new Variable("x"), new Variable("y"));
        
        Expression expected = new Addition(multExpr1, multExpr2);
        Expression addition = multExpr1.add(multExpr2);
        
        assertNotEquals("Expected non-null", null, multExpr1);
        assertNotEquals("Expected non-null", null, multExpr2);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected addition expression", 
                expected, addition);
    }

    // Tests for variant.multiply(otherVariant)
    @Test
    // covers value * value
    public void testMultiply_Values() {
        // 2.2*2.2
        Value valueExpr = new Value("2.2");
        
        Expression expected = new Multiplication(valueExpr, valueExpr);
        Expression product = valueExpr.multiply(valueExpr);
        
        assertNotEquals("Expected non-null", null, valueExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected product expression", 
                expected, product);
    }
    @Test
    // covers variable * variable
    public void testMultiply_Variables() {
        // x*x
        Variable varExpr = new Variable("x");        
        Expression expected = new Multiplication(varExpr, varExpr);
        Expression product = varExpr.multiply(varExpr);
        
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected product expression", 
                expected, product);
    }
    @Test
    // covers variable * value
    public void testMultiply_VariableValue() {
        // x*2.2
        Variable varExpr = new Variable("x");
        Value valueExpr = new Value("2.2");
        
        Expression expected = new Multiplication(varExpr, valueExpr);
        Expression product = varExpr.multiply(valueExpr);
        
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, valueExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected product expression", 
                expected, product);
    }
    @Test
    // covers sum * variable
    public void testMultiply_SumVariable() {
        // x*(x + y)
        Variable varExpr = new Variable("x");
        Addition sumExpr = 
                new Addition(varExpr, new Variable("y"));
        
        Expression expected = new Multiplication(sumExpr, varExpr);
        Expression product = sumExpr.multiply(varExpr);
        
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, sumExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected product expression", 
                expected, product);        
    }
    @Test
    // covers variable * product
    public void testMultiply_VariableProduct() {
        // x*(x*y)
        Variable varExpr = new Variable("x");
        Multiplication multExpr =
                new Multiplication(varExpr, new Variable("y"));
        
        Expression expected = new Multiplication(varExpr, multExpr);
        Expression product = varExpr.multiply(multExpr);
        
        assertNotEquals("Expected non-null", null, varExpr);
        assertNotEquals("Expected non-null", null, multExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected product expression", 
                expected, product);
    }
    @Test
    // covers sum * sum
    public void testMultiply_Sums() {
        // (x + 2)*(x + y)
        Addition leftSumExpr = 
                new Addition(new Variable("x"), new Value("2"));
        Addition rightSumExpr = 
                new Addition(new Variable("x"), new Variable("y"));
        
        Expression expected =
                new Multiplication(leftSumExpr, rightSumExpr);
        Expression product = leftSumExpr.multiply(rightSumExpr);
        
        assertNotEquals("Expected non-null", null, leftSumExpr);
        assertNotEquals("Expected non-null", null, rightSumExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected addition expression", 
                expected, product);
    }
    @Test
    // covers sum * product
    public void testMultiply_SumProduct() {
        // (x + 2)*(x*y)
        Addition sumExpr = 
                new Addition(new Variable("x"), new Value("2"));
        Multiplication multExpr = 
                new Multiplication(new Variable("x"), new Variable("y"));
        
        Expression expected = 
                new Multiplication(sumExpr, multExpr);
        Expression product = sumExpr.multiply(multExpr);
        
        assertNotEquals("Expected non-null", null, multExpr);
        assertNotEquals("Expected non-null", null, sumExpr);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected product expression", 
                expected, product);
    }
    @Test
    // covers product * product
    public void testMultiply_Products() {
        // (2*x)*(x*y)
        Multiplication multExpr1 = 
                new Multiplication(new Value("2"), new Variable("x"));
        Multiplication multExpr2 = 
                new Multiplication(new Variable("x"), new Variable("y"));
        
        Expression expected = 
                new Multiplication(multExpr1, multExpr2);
        Expression product = multExpr1.multiply(multExpr2);
        
        assertNotEquals("Expected non-null", null, multExpr1);
        assertNotEquals("Expected non-null", null, multExpr2);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected product expression", 
                expected, product);
    }
    
    // Tests for variant.getSubExpr()
    @Test
    // covers addition
    public void testGetSubExpr_Addition() {
        // (x + 2) + (x*y)
        Addition sumExpr = 
                new Addition(new Variable("x"), new Value("2"));
        Multiplication multExpr = 
                new Multiplication(new Variable("x"), new Variable("y"));
        Expression addition = sumExpr.add(multExpr);
        
        List<Expression> expected = Arrays.asList(sumExpr, multExpr);
        List<Expression> subExpr = addition.getSubExpr();
        
        assertNotEquals("Expected non-empty list",
                Collections.emptyList(), subExpr);
        assertEquals("Expected doubleton list",
                2, subExpr.size());
        assertEquals("Expected correct operands",
                expected, subExpr);
    }
    @Test
    // covers multiplication
    public void testGetSubExpr_Mult() {
        // (x + 2)*(x*y)
        Addition sumExpr = 
                new Addition(new Variable("x"), new Value("2"));
        Multiplication multExpr = 
                new Multiplication(new Variable("x"), new Variable("y"));
        Expression product = sumExpr.multiply(multExpr);
        
        List<Expression> expected = Arrays.asList(sumExpr, multExpr);
        List<Expression> subExpr = product.getSubExpr();
        
        assertNotEquals("Expected non-empty list",
                Collections.emptyList(), subExpr);
        assertEquals("Expected doubleton list",
                2, subExpr.size());
        assertEquals("Expected correct operands",
                expected, subExpr);
    }
    
    // Tests for variant.toString()
    //TODO: include parse tests to check if the string is parsable
    @Test
    // covers value
    public void testToString_Value() {
        Value valueExpr = new Value("2.2");
        
        assertEquals("Expected correct string rep", 
                "2.2", valueExpr.toString());
    }
    @Test
    // covers variable
    public void testToString_Variable() {
        Variable varExpr = new Variable("Foo");
        
        assertEquals("Expected correct string rep", 
                "Foo", varExpr.toString());
    }
    @Test
    // covers addition
    public void testToString_Addition() {
        Addition sumExpr = 
                new Addition(new Variable("x"), new Value("2"));
        Multiplication multExpr = 
                new Multiplication(new Variable("x"), new Variable("y"));
        Expression addition = sumExpr.add(multExpr);
        
        String expected = "x + 2 + x*y";
        String sumString = addition.toString();
        
        assertEquals("Expected correct string rep",
                expected, sumString);
    }
    @Test
    // covers multiplication
    public void testToString_Mult() {
        Addition leftSumExpr = 
                new Addition(new Variable("x"), new Value("2"));
        Addition rightSumExpr = 
                new Addition(new Variable("x"), new Variable("y"));
        Expression multExpr =
                new Multiplication(leftSumExpr, rightSumExpr);
        
        String expected = "(x + 2)*(x + y)";
        String multString = multExpr.toString();
        
        assertEquals("Expected correct string rep",
                expected, multString);
    }

    // Tests for variant.equals(variant)
    @Test
    // covers value
    public void testEquals_Value() {
        Value value1 = new Value("1");
        Value value2 = new Value("1.0");
        Value value3 = new Value("1.000");
        Value value4 = new Value("1.02");
        
        assertFalse("Expected equality in precision",
                value4.equals(value1));
        //reflexive
        assertTrue("Expected reflexive equality", 
                value1.equals(value1));
        assertTrue("Expected reflexive equality", 
                value2.equals(value2));
        //symmetric
        assertTrue("Expected symmetric equality", 
                value1.equals(value2) && value2.equals(value1));
        assertTrue("Expected symmetric equality", 
                value1.equals(value3) && value3.equals(value1));
        //transitive
        if (value1.equals(value3) && value1.equals(value2)) {
            assertTrue("Expected transitive equality", 
                    value3.equals(value2));
        }
        
    }
    @Test
    // covers variable
    public void testEquals_Variable() {
        Variable var1 = new Variable("Foo");
        Variable var2 = new Variable("Foo");
        Variable var3 = new Variable("Foo");
        Variable var4 = new Variable("foo");
        
        assertFalse("Expected variable to be case-sensitive",
                var4.equals(var1));
        //reflexive
        assertTrue("Expected reflexive equality", 
                var1.equals(var1));
        assertTrue("Expected reflexive equality", 
                var2.equals(var2));
        //symmetric
        assertTrue("Expected symmetric equality", 
                var1.equals(var2) && var2.equals(var1));
        assertTrue("Expected symmetric equality", 
                var1.equals(var3) && var3.equals(var1));
        //transitive
        if (var1.equals(var3) && var1.equals(var2)) {
            assertTrue("Expected transitive equality", 
                    var3.equals(var2));
        }
    }
    @Test
    // covers Addition
    public void testEquals_Addition() {
        // x + x*y == x + x*y
        // x + x*y != x + y*x
        // x + x*y != x*y + x
        // 1 + 2.0 != 2 + 1
        // x + x*y != (x + x)*y
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Multiplication mult1 = 
                new Multiplication(x, y);// x*y
        Multiplication mult2 = 
                new Multiplication(y, x);// y*x
        Multiplication mult3 =
                new Multiplication(new Addition(x, x), y);// (x + x)*y
        Addition add1 = 
                new Addition(x, mult1);// x + x*y
        Addition add2 = 
                new Addition(x, mult1);// x + x*y
        Addition add3 = 
                new Addition(x, mult2);// x + y*x
        Addition add4 = 
                new Addition(mult1, x);// x*y + x
        Addition add5 =
                new Addition(new Value("1"), new Value("2.0"));// 1 + 2.0
        Addition add6 =
                new Addition(new Value("2"), new Value("1"));// 2 + 1
        
        assertTrue("Expected reflexive equality", 
                add1.equals(add1));
        assertTrue("Expected symmetric equality",
                add1.equals(add2) && add2.equals(add1));
        assertFalse("Expected equal sums to have same order of variables", 
                add1.equals(add3));
        assertFalse("Expected equal sums to have same order of operators", 
                add1.equals(add4));
        assertFalse("Expected equal sums to have same order of numbers", 
                add5.equals(add6));
        assertFalse("Expected equal sums to have same grouping", 
                add1.equals(mult3));
    }
    @Test
    // covers multiplication
    public void testEquals_Multiplication() {
        // x*(x + y) == x*(x + y)
        // x*(x + y) != x*(y + x)
        // x*(x + y) != (x + y)*x
        // 1*2.0 != 2*1
        // x*(x + y) != x*x + y
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Addition add1 = 
                new Addition(x, y);// x + y
        Addition add2 = 
                new Addition(y, x);// y + x
        Addition add3 = 
                new Addition(new Multiplication(x,x), y);// x*x + y
        Multiplication mult1 = 
                new Multiplication(x, add1);// x*(x + y)
        Multiplication mult2 = 
                new Multiplication(x, add1);// x*(x + y)
        Multiplication mult3 =
                new Multiplication(x, add2);// x*(y + x)
        Multiplication mult4 =
                new Multiplication(add1, x);// (x + y)*x
        Multiplication mult5 =
                new Multiplication(new Value("1"), new Value("2.0"));// 1*2.0
        Multiplication mult6 =
                new Multiplication(new Value("2"), new Value("1"));// 2*1
        
        assertTrue("Expected reflexive equality", 
                mult1.equals(mult1));
        assertTrue("Expected symmetric equality",
                mult1.equals(mult2) && mult2.equals(mult1));
        assertFalse("Expected equal products to have same order of variables", 
                mult1.equals(mult3));
        assertFalse("Expected equal products to have same order of operators", 
                mult1.equals(mult4));
        assertFalse("Expected equal products to have same order of numbers", 
                mult5.equals(mult6));
        assertFalse("Expected equal products to have same grouping", 
                mult1.equals(add3));
    }
    
    // Tests for variant.hashCode()
    @Test
    // covers value
    public void testHashCode_Value() {
        Value value1 = new Value("123");
        Value value2 = new Value("123.000");
        
        assertTrue("Expected values to be equal",
                value1.equals(value2));
        assertEquals("Expected equal values to have equal hashcodes", 
                value1.hashCode(), value2.hashCode());
    }
    @Test
    // covers variable
    public void testHashCode_Variable() {
        Variable var1 = new Variable("Foobar");
        Variable var2 = new Variable("Foobar");
      
        assertTrue("Expected variables to be equal",
                var1.equals(var2));
        assertEquals("Expected equal variables to have equal hashcodes", 
                var1.hashCode(), var2.hashCode());
    }
    @Test
    // covers addition
    public void testHashCode_Addition() {
        Variable x = new Variable("x");
        Value val = new Value("2");
        Multiplication mult = 
                new Multiplication(x, val);// x*2
        Addition add1 = 
                new Addition(x, mult);// x + x*2
        Addition add2 = 
                new Addition(x, mult);// x + x*2
      
        assertTrue("Expected sums to be equal",
                add1.equals(add2));
        assertEquals("Expected equal sums to have equal hashcodes", 
                add1.hashCode(), add2.hashCode());
    }
    @Test
    public void testHashCode_Multiplication() {
        Variable x = new Variable("x");
        Value val = new Value("2");
        Addition add = 
                new Addition(x, val);// x + 2
        Multiplication mult1 = 
                new Multiplication(x, add);// x*(x + 2)
        Multiplication mult2 = 
                new Multiplication(x, add);// x*(x + 2)
        
        assertTrue("Expected products to be equal", 
                mult1.equals(mult2));
        assertEquals("Expected equal products to have equal hashcodes", 
                mult1.hashCode(), mult2.hashCode());
    }
    
}
