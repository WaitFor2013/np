
package com.np.database.sql.visitor.functions;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_ERROR;
import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;
import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE_NULL;

import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;

public class Isnull implements Function {

    public final static Isnull instance = new Isnull();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        final List<SQLExpr> parameters = x.getParameters();
        if (parameters.size() == 0) {
            return EVAL_ERROR;
        }

        SQLExpr condition = parameters.get(0);
        condition.accept(visitor);
        Object itemValue = condition.getAttributes().get(EVAL_VALUE);
        if (itemValue == EVAL_VALUE_NULL) {
            return Boolean.TRUE;
        } else if (itemValue == null) {
            return null;
        } else {
            return Boolean.FALSE;
        }
    }
}
