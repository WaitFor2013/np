
package com.np.database.sql.visitor.functions;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;

import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;

public class Substring implements Function {

    public final static Substring instance = new Substring();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        List<SQLExpr> params = x.getParameters();
        int paramSize = params.size();

        SQLExpr param0 = params.get(0);

        SQLExpr param1;
        if (paramSize == 1 && x.getFrom() != null) {
            param1 = x.getFrom();
            paramSize = 2;
        } else if (paramSize != 2 && paramSize != 3) {
            return SQLEvalVisitor.EVAL_ERROR;
        } else {
            param1 = params.get(1);
        }

        param0.accept(visitor);
        param1.accept(visitor);

        Object param0Value = param0.getAttributes().get(EVAL_VALUE);
        Object param1Value = param1.getAttributes().get(EVAL_VALUE);
        if (param0Value == null || param1Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }

        String str = param0Value.toString();
        int index = ((Number) param1Value).intValue();

        if (paramSize == 2 && x.getFor() == null) {
            if (index <= 0) {
                int lastIndex = str.length() + index;
                return str.substring(lastIndex);
            }

            return str.substring(index - 1);
        }

        SQLExpr param2 = x.getFor();
        if (param2 == null && params.size() > 2) {
            param2 = params.get(2);
        }
        param2.accept(visitor);
        Object param2Value = param2.getAttributes().get(EVAL_VALUE);
        if (param2Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }

        int len = ((Number) param2Value).intValue();

        String result;
        if (index <= 0) {
            int lastIndex = str.length() + index;
            result = str.substring(lastIndex);
        } else {
            result = str.substring(index - 1);
        }

        if (len > result.length()) {
            return result;
        }
        return result.substring(0, len);
    }
}
