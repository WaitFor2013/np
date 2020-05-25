
package com.np.database.sql.visitor.functions;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;

import java.math.BigDecimal;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;

public class Char implements Function {

    public final static Char instance = new Char();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        if (x.getParameters().size() == 0) {
            return SQLEvalVisitor.EVAL_ERROR;
        }

        StringBuffer buf = new StringBuffer(x.getParameters().size());
        for (SQLExpr param : x.getParameters()) {
            param.accept(visitor);

            Object paramValue = param.getAttributes().get(EVAL_VALUE);

            if (paramValue instanceof Number) {
                int charCode = ((Number) paramValue).intValue();
                buf.append((char) charCode);
            } else if (paramValue instanceof String) {
                try {
                    int charCode = new BigDecimal((String) paramValue).intValue();
                    buf.append((char) charCode);
                } catch (NumberFormatException e) {
                }
            } else {
                return SQLEvalVisitor.EVAL_ERROR;
            }
        }

        return buf.toString();
    }
}
