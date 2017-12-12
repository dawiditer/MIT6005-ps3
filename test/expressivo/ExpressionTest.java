/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package expressivo;

import static org.junit.Assert.*;
import org.junit.Test;
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
    //    variant.toString()
    //      include parse tests to make sure the string is parsable
    //    variant.equals(variant)
    //      - reflexive
    //      - symmetric
    //      - transitive
    //    variant.hashCode()
    //    variant.isAddition()
    //
    // Testing Strategy for variant.differentiate(inputVariable)
    //  Partition the input as follows:
    //   - all variants as input
    //   - Addition and Multiplication variants to include multiple variables
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
        Expression parsed = Expression.parse(input);
        Expression expected = 
                new Addition(new Value("2"), new Variable("x"));
        Expression wrongOrder = 
                new Addition(new Variable("x"), new Value("2"));
        
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
        Expression parsed = Expression.parse(input);
        Expression expected = new Addition(
                new Addition(new Variable("x"), new Variable("x")),
                new Variable("y"));
   
        assertNotEquals("Expected non-null expression", 
                null, parsed);
        assertEquals("Expected correct parse",
                expected, parsed);
    }
    @Test
    // covers single multiplication expression
    public void testParse_SingleMult() {
        String input = "x*y";
        Expression parsed = Expression.parse(input);
        Expression expected =
                new Multiplication(new Variable("x"), new Variable("y"));
        Expression wrongOrder =
                new Multiplication(new Variable("y"), new Variable("x"));
        
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
        Expression parsed = Expression.parse(input);
        Variable x = new Variable("x");
        Value num = new Value("2");
        Expression expected = 
                new Multiplication(
                        new Multiplication(x, num), x);
        
        assertNotEquals("Expected non-null expression", 
                null, parsed);
        assertEquals("Expected correct parse",
                expected, parsed);
    }
    @Test
    // covers addition, multiplication and grouping
    public void testParse_AddMult() {
        String input = "(x + (2.12*x))*y";
        Expression parsed = Expression.parse(input);
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Value num = new Value("2.12");
        Multiplication group1 =
                new Multiplication(num, x);
        Addition group2 =
                new Addition(x, group1);
        Expression expected = 
                new Multiplication(group2, y);
        
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
        Expression addition = varExpr.add(varExpr);
        Expression expected = new Addition(varExpr, varExpr);
        
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
        
        Expression addition = valueExpr.add(varExpr);
        Expression expected = new Addition(valueExpr, varExpr);
        
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
        
        Expression addition = sumExpr.add(varExpr);
        Expression expected = new Addition(sumExpr, varExpr);
        
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
        
        Expression addition = varExpr.add(multExpr);
        Expression expected = new Addition(varExpr, multExpr);
        
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
        
        Expression addition = leftSumExpr.add(rightSumExpr);
        Expression expected = new Addition(leftSumExpr, rightSumExpr);
        
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
        
        Expression addition = sumExpr.add(multExpr);
        Expression expected = new Addition(sumExpr, multExpr);
        
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
        
        Expression addition = multExpr1.add(multExpr2);
        Expression expected = new Addition(multExpr1, multExpr2);
        
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
        
        Expression product = valueExpr.multiply(valueExpr);
        Expression expected = new Multiplication(valueExpr, valueExpr);
        
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
        Expression product = varExpr.multiply(varExpr);
        Expression expected = new Multiplication(varExpr, varExpr);
        
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
        
        Expression product = varExpr.multiply(valueExpr);
        Expression expected = new Multiplication(varExpr, valueExpr);
        
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
        
        Expression product = sumExpr.multiply(varExpr);
        Expression expected = new Multiplication(sumExpr, varExpr);
        
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
        
        Expression product = varExpr.multiply(multExpr);
        Expression expected = new Multiplication(varExpr, multExpr);
        
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
        
        Expression product = leftSumExpr.multiply(rightSumExpr);
        Expression expected =
                new Multiplication(leftSumExpr, rightSumExpr);
        
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
        
        Expression product = sumExpr.multiply(multExpr);
        Expression expected = 
                new Multiplication(sumExpr, multExpr);
        
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
        
        Expression product = multExpr1.multiply(multExpr2);
        Expression expected = 
                new Multiplication(multExpr1, multExpr2);
        
        assertNotEquals("Expected non-null", null, multExpr1);
        assertNotEquals("Expected non-null", null, multExpr2);
        assertNotEquals("Expected non-null", null, expected);
        assertEquals("Expected product expression", 
                expected, product);
    }
    
    // Tests for variant.toString()
    @Test
    // covers value
    public void testToString_Value() {
        Value valueExpr = new Value("2.2");
        
        assertEquals("Expected correct string rep", 
                "2.2", valueExpr.toString());
        assertEquals("Expected parsable string rep", 
                valueExpr, Expression.parse(valueExpr.toString()));
    }
    @Test
    // covers variable
    public void testToString_Variable() {
        Variable varExpr = new Variable("Foo");
        
        assertEquals("Expected correct string rep", 
                "Foo", varExpr.toString());
        assertEquals("Expected parsable string rep", 
                varExpr, Expression.parse(varExpr.toString()));
    }
    @Test
    // covers addition
    public void testToString_Addition() {
        Addition sumExpr = 
                new Addition(new Variable("x"), new Value("2"));
        Multiplication multExpr = 
                new Multiplication(new Variable("x"), new Variable("y"));
        Expression addition = sumExpr.add(multExpr);
        
        String sumString = addition.toString();
        String expected = "x + 2 + x*y";
        
        assertEquals("Expected correct string rep",
                expected, sumString);
        assertEquals("Expected parsable string rep", 
                addition, Expression.parse(addition.toString()));
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
        
        String multString = multExpr.toString();
        String expected = "(x + 2)*(x + y)";
        
        assertEquals("Expected correct string rep",
                expected, multString);
        assertEquals("Expected parsable string rep", 
                multExpr, Expression.parse(multExpr.toString()));
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
        // (x + y) + x == x + (y + x)
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
        Addition add7 = new Addition(
                new Addition(x, y), x);// (x + y) + x
        Addition add8 = new Addition(
                x, new Addition(y, x));// x + (y + x)
        
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
        assertTrue("Expected sums with same meaning to be equal",
                add7.equals(add8));
    }
    @Test
    // covers multiplication
    public void testEquals_Multiplication() {
        // x*(x + y) == x*(x + y)
        // x*(x + y) != x*(y + x)
        // x*(x + y) != (x + y)*x
        // 1*2.0 != 2*1
        // x*(x + y) != x*x + y
        // x*x*(y) == x*(x*y)
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
        Multiplication mult7 =
                new Multiplication(
                        new Multiplication(x, x),
                        y); // x*x*(y)
        Multiplication mult8 =
                new Multiplication(
                        x,
                        new Multiplication(x, y)); // x*(x*y)
        
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
        assertTrue("Expected products with same meaning to be equal", 
                mult7.equals(mult8));
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
    
    // Tests for variant.isAddition()
    @Test
    // covers value
    public void testIsAddition_Value() {
        Value valueExpr = new Value("3.142");
        
        assertFalse("Expected value not to be an addition", 
                valueExpr.isAddition());
    }
    @Test
    // covers variable
    public void testIsAddition_Variable() {
        Variable varExpr = new Variable("foo");
        
        assertFalse("Expected variable not to be an addition", 
                varExpr.isAddition());
    }
    @Test
    // covers addition
    public void testIsAddition_Addition() {
        Addition addExpr = new Addition(new Variable("x"), new Value("0"));
        
        assertTrue("Expected addition to be an addition", 
                addExpr.isAddition());
    }  
    @Test
    // covers multiplication
    public void testIsAddition_Multiplication() {
        Multiplication multExpr = new Multiplication(new Value("0.24"), new Value("0.13"));
        
        assertFalse("Expected multiplication not to be an addition", 
                multExpr.isAddition());
    }
 // Tests for variant.differentiate(inputVariable)
    @Test
    // covers value
    public void testDifferentiate_Value() {
        Value valueExpr = new Value("2.1");
        Expression deriv = valueExpr.differentiate("x");
        Expression expected = new Value("0");
        
        assertNotEquals("Expected non-null expression", 
                null, deriv);
        assertEquals("Expected derivative of a constant to be 0", 
                expected, deriv);
    }
    @Test
    // covers variable
    public void testDifferentiate_Variable() {
        Variable valueExpr = new Variable("foo");
        Expression deriv1 = valueExpr.differentiate("foo");
        Expression deriv2 = valueExpr.differentiate("bar");
        Expression expected1 = new Value("1");
        Expression expected2 = new Value("0");
        
        assertNotEquals("Expected non-null expression", 
                null, deriv1);
        assertNotEquals("Expected non-null expression", 
                null, deriv2);
        assertEquals("Expected derivative with repect to itself to be 1", 
                expected1, deriv1);
        assertEquals("Expected derivative with repect to another variable to be 0", 
                expected2, deriv2);
    }
    @Test
    // covers addition
    public void testDifferentiate_Addition() {
        Addition addExpr = new Addition(
                new Addition(new Variable("x"), new Value("3.142")), 
                new Variable("y"));
        Expression deriv = addExpr.differentiate("y");
        Expression expected = new Addition(
                new Addition(new Value("0"), new Value("0")),
                new Value("1"));
        
        System.out.println(deriv);
        assertNotEquals("Expected non-null expression", 
                null, deriv);
        assertEquals("Expected derivative of an addition", 
                expected, deriv);
    }
    @Test
    // covers multiplication
    public void testDifferentiate_Multiplication() {
        Variable x = new Variable("x");
        Value one = new Value("1");
        Multiplication multExpr = new Multiplication(
                new Multiplication(x,x), 
                x);
        Expression deriv = multExpr.differentiate("x");
                
        Multiplication multGroup1 = new Multiplication(x, one);    // x*1
        Multiplication multGroup2 = new Multiplication( 
                x, new Addition(multGroup1, multGroup1));          // x*(x*1 + x*1)
        Multiplication multGroup3 = new Multiplication(
                x, multGroup1);                                    // x*x*1
        Expression expected = new Addition(multGroup2, multGroup3);// x*(x*1 + x*1) + x*x*1
        
        assertNotEquals("Expected non-null expression", 
                null, expected);
        assertEquals("Expected derivative of a multiplication", 
                expected, deriv);
        
    }
}