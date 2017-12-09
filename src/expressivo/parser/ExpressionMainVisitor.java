package expressivo.parser;

import expressivo.Addition;
import expressivo.Expression;
import expressivo.Multiplication;
import expressivo.Value;
import expressivo.Variable;

public class ExpressionMainVisitor extends ExpressionBaseVisitor<Expression> {
    /** Creates an addition expression on every visit */
    @Override public Expression visitAdd(ExpressionParser.AddContext ctx) {
        Expression leftExpr = visit(ctx.expr(0));
        Expression rightExpr = visit(ctx.expr(1));
        
        return new Addition(leftExpr, rightExpr);
    }

    /** Creates a multiplication expression on every visit */
    @Override public Expression visitMult(ExpressionParser.MultContext ctx) {
        Expression leftExpr = visit(ctx.expr(0));
        Expression rightExpr = visit(ctx.expr(1));
        
        return new Multiplication(leftExpr, rightExpr);
    }
    /** Creates a number on every visit */
    @Override public Expression visitNum(ExpressionParser.NumContext ctx) {
        String num = ctx.NUM().getText();
        
        return new Value(num); 
    }

    /** Creates a variable on every visit */
    @Override public Expression visitId(ExpressionParser.IdContext ctx) { 
        String id = ctx.ID().getText();
        
        return new Variable(id);
    }
}
