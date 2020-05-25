
package com.np.database.sql.visitor.functions;

import static com.np.database.sql.visitor.SQLEvalVisitor.EVAL_VALUE;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;

public class Ltrim implements Function {

    public final static Ltrim instance = new Ltrim();

    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        if (x.getParameters().size() != 1) {
            return SQLEvalVisitor.EVAL_ERROR;
        }

        SQLExpr param0 = x.getParameters().get(0);
        param0.accept(visitor);

        Object param0Value = param0.getAttributes().get(EVAL_VALUE);
        if (param0Value == null) {
            return SQLEvalVisitor.EVAL_ERROR;
        }

        String strValue = param0Value.toString();
        
        int index = -1;
        for (int i = 0; i < strValue.length(); ++i) {
            if (!Character.isWhitespace(strValue.charAt(i))) {
                index = i;
                break;
            }
        }
        
        if (index <= 0) {
            return strValue;
        } else {
            return strValue.substring(index);
        }
    }
}
