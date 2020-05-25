
package com.np.database.sql.visitor.functions;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.visitor.SQLEvalVisitorUtils;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;

public class Least implements Function {

    public final static Least instance = new Least();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        Object result = null;
        for (SQLExpr item : x.getParameters()) {
            item.accept(visitor);

            Object itemValue = item.getAttributes().get(EVAL_VALUE);
            if (result == null) {
                result = itemValue;
            } else {
                if (SQLEvalVisitorUtils.lt(itemValue, result)) {
                    result = itemValue;
                }
            }
        }

        return result;
    }
}
