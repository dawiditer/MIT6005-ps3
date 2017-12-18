package expressivo.parser;

import expressivo.Expression;

public class ExpressionMainVisitor extends ExpressionBaseVisitor<Expression> {
    /** Returns the whole expression generated from the parse tree */
    @Override public Expression visitRoot(ExpressionParser.RootContext ctx) { 
        return visit(ctx.stat()); 
    }
    /** Returns the whole expression generated from the parse tree */
    @Override public Expression visitStat(ExpressionParser.StatContext ctx) { 
        return visit(ctx.expr()); 
    }
    /** Creates an addition expression on every visit */
    @Override public Expression visitAdd(ExpressionParser.AddContext ctx) {
        Expression leftExpr = visit(ctx.expr(0));
        Expression rightExpr = visit(ctx.expr(1));
        
        return leftExpr.addExpr(rightExpr);
    }
    /** Returns the expression inside braces */
    @Override public Expression visitBrackets(ExpressionParser.BracketsContext ctx) {
        return visit(ctx.expr()); 
    }
    /** Creates a multiplication expression on every visit */
    @Override public Expression visitMult(ExpressionParser.MultContext ctx) {
        Expression leftExpr = visit(ctx.expr(0));
        Expression rightExpr = visit(ctx.expr(1));
        
        return leftExpr.multiplyExpr(rightExpr);
    }
    /** Creates a number on every visit */
    @Override public Expression visitNum(ExpressionParser.NumContext ctx) {
        String num = ctx.NUM().getText();
        
        Expression empty = Expression.emptyExpression();
        return empty.addConstant(Double.parseDouble(num)); 
    }

    /** Creates a variable on every visit */
    @Override public Expression visitId(ExpressionParser.IdContext ctx) { 
        String id = ctx.ID().getText();
        
        Expression empty = Expression.emptyExpression();
        return empty.addVariable(id);
    }
}
