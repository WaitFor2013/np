
package com.np.database.sql.visitor.functions;

import java.util.Date;

import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.visitor.SQLEvalVisitor;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;


public class Now implements Function {
    public final static Now instance = new Now();
    
    public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
        return new Date();
    }
}
