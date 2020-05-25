
package com.np.database.sql.dialect.oracle.visitor;

import com.np.database.sql.ast.expr.SQLBinaryOpExpr;
import com.np.database.sql.visitor.ParameterizedVisitor;
import com.np.database.sql.visitor.VisitorFeature;
import com.np.database.sql.ast.expr.SQLBinaryOpExpr;
import com.np.database.sql.visitor.ParameterizedVisitor;
import com.np.database.sql.visitor.VisitorFeature;

public class OracleParameterizedOutputVisitor extends OracleOutputVisitor implements ParameterizedVisitor {

    public OracleParameterizedOutputVisitor(){
        this(new StringBuilder());
        this.config(VisitorFeature.OutputParameterized, true);
    }

    public OracleParameterizedOutputVisitor(Appendable appender){
        super(appender);
        this.config(VisitorFeature.OutputParameterized, true);
    }

    public OracleParameterizedOutputVisitor(Appendable appender, boolean printPostSemi){
        super(appender, printPostSemi);
        this.config(VisitorFeature.OutputParameterized, true);
    }

    public boolean visit(SQLBinaryOpExpr x) {
        x = SQLBinaryOpExpr.merge(this, x);

        return super.visit(x);
    }

//    public boolean visit(SQLNumberExpr x) {
//        print('?');
//        incrementReplaceCunt();
//
//        if(this instanceof ExportParameterVisitor || this.parameters != null){
//            ExportParameterVisitorUtils.exportParameter((this).getParameters(), x);
//        }
//        return false;
//    }

}
