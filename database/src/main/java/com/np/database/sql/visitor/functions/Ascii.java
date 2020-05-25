
package com.np.database.sql.visitor.functions;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;
import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE_NULL;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;

public class Ascii implements Function {

    public final static Ascii instance = new Ascii();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        if (x.getParameters().size() == 0) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        SQLExpr param = x.getParameters().get(0);
        param.accept(visitor);
        
        Object paramValue = param.getAttributes().get(EVAL_VALUE);
        if (paramValue == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }
        
        if (paramValue == EVAL_VALUE_NULL) {
            return EVAL_VALUE_NULL;
        }

        String strValue = paramValue.toString();
        if (strValue.length() == 0) {
            return 0;
        }

        int ascii = strValue.charAt(0);
        return ascii;
    }
}
