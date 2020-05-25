
package com.np.database.sql.visitor.functions;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;

public class Concat implements Function {

    public final static Concat instance = new Concat();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        StringBuilder buf = new StringBuilder();

        for (SQLExpr item : x.getParameters()) {
            item.accept(visitor);

            Object itemValue = item.getAttribute(EVAL_VALUE);
            if (itemValue == null) {
                return null;
            }
            buf.append(itemValue.toString());
        }

        return buf.toString();
    }
}
