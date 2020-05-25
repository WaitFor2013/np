
package com.np.database.sql.visitor.functions;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.util.NpHexBin;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.util.NpHexBin;

import java.io.UnsupportedEncodingException;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_EXPR;
import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;

public class Unhex implements Function {

    public final static Unhex instance = new Unhex();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        if (x.getParameters().size() != 1) {
            return SQLEvalVisitor.EVAL_ERROR;
        }

        SQLExpr param0 = x.getParameters().get(0);

        if (param0 instanceof SQLMethodInvokeExpr) {
            SQLMethodInvokeExpr paramMethodExpr = (SQLMethodInvokeExpr) param0;
            if (paramMethodExpr.getMethodName().equalsIgnoreCase("hex")) {
                SQLExpr subParamExpr = paramMethodExpr.getParameters().get(0);
                subParamExpr.accept(visitor);

                Object param0Value = subParamExpr.getAttributes().get(EVAL_VALUE);
                if (param0Value == null) {
                    x.putAttribute(EVAL_EXPR, subParamExpr);
                    return SQLEvalVisitor.EVAL_ERROR;
                }

                return param0Value;
            }
        }

        param0.accept(visitor);

        Object param0Value = param0.getAttributes().get(EVAL_VALUE);
        if (param0Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }

        if (param0Value instanceof String) {
            byte[] bytes = NpHexBin.decode((String) param0Value);
            if (bytes == null) {
                return SQLEvalVisitor.EVAL_VALUE_NULL;
            }
            
            String result;
            try {
                result = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
            return result;
        }

        return SQLEvalVisitor.EVAL_ERROR;
    }
}
