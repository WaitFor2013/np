
package com.np.database.sql.visitor.functions;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_ERROR;
import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;

import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.visitor.SQLEvalVisitorUtils;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;

public class If implements Function {

    public final static If instance = new If();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        final List<SQLExpr> parameters = x.getParameters();
        if (parameters.size() == 0) {
            return EVAL_ERROR;
        }

        SQLExpr condition = parameters.get(0);
        condition.accept(visitor);
        Object itemValue = condition.getAttributes().get(EVAL_VALUE);
        if (itemValue == null) {
            return null;
        }
        if (Boolean.TRUE == itemValue || !SQLEvalVisitorUtils.eq(itemValue, 0)) {
            SQLExpr trueExpr = parameters.get(1);
            trueExpr.accept(visitor);
            return trueExpr.getAttributes().get(EVAL_VALUE);
        } else {
            SQLExpr falseExpr = parameters.get(2);
            falseExpr.accept(visitor);
            return falseExpr.getAttributes().get(EVAL_VALUE);
        }
    }
}
