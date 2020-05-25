
package com.np.database.sql.visitor.functions;

import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;


public interface Function {
    Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x);
}
