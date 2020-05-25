
package com.np.database.sql.visitor.functions;

import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;

public class Nil implements Function {

    public final static Nil instance = new Nil();

    @Override
    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        return null;
    }

}
