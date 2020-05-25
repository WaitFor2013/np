
package com.np.database.sql.dialect.mysql.visitor;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.ast.SQLOrderBy;
import com.np.database.sql.ast.expr.SQLBetweenExpr;
import com.np.database.sql.ast.expr.SQLBinaryOpExpr;
import com.np.database.sql.ast.expr.SQLInListExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.ast.statement.SQLSelectGroupByClause;
import com.np.database.sql.ast.statement.SQLSelectItem;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlFlushStatement;
import com.np.database.sql.visitor.ExportParameterVisitor;
import com.np.database.sql.visitor.ExportParameterVisitorUtils;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.ast.SQLOrderBy;
import com.np.database.sql.ast.expr.SQLBetweenExpr;
import com.np.database.sql.ast.expr.SQLBinaryOpExpr;
import com.np.database.sql.ast.expr.SQLInListExpr;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.ast.statement.SQLSelectGroupByClause;
import com.np.database.sql.ast.statement.SQLSelectItem;
import com.np.database.sql.visitor.ExportParameterVisitor;
import com.np.database.sql.visitor.ExportParameterVisitorUtils;

public class MySqlExportParameterVisitor extends MySqlOutputVisitor implements ExportParameterVisitor {

    /**
     * true= if require parameterized sql output
     */
    private boolean requireParameterizedOutput;


    public MySqlExportParameterVisitor(List<Object> parameters, Appendable appender, boolean wantParameterizedOutput){
        super(appender, true);
        this.parameters = parameters;
        this.requireParameterizedOutput = wantParameterizedOutput;
    }

    public MySqlExportParameterVisitor() {
        this(new ArrayList<Object>());
    }

    public MySqlExportParameterVisitor(List<Object> parameters) {
        this(parameters, null, false);
    }

    public MySqlExportParameterVisitor(final Appendable appender) {
        this(new ArrayList<Object>(),appender, true);
    }

    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public boolean visit(final SQLSelectItem x) {
        if(requireParameterizedOutput){
            return super.visit(x);
        }
        return true;
    }

    @Override
    public boolean visit(SQLLimit x) {
        if(requireParameterizedOutput){
            return super.visit(x);
        }

        return true;
    }

    @Override
    public boolean visit(SQLOrderBy x) {
        if(requireParameterizedOutput){
            return super.visit(x);
        }
        return false;
    }

    @Override
    public boolean visit(SQLSelectGroupByClause x) {
        if(requireParameterizedOutput){
            return super.visit(x);
        }
        return false;
    }

    @Override
    public boolean visit(SQLMethodInvokeExpr x) {
        if(requireParameterizedOutput){
           return super.visit(x);
        }
        
        ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getParameters());
        return true;
    }

    @Override
    public boolean visit(SQLInListExpr x) {
        if(requireParameterizedOutput){
            return super.visit(x);
         }
        ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getTargetList());

        return true;
    }

    @Override
    public boolean visit(SQLBetweenExpr x) {
        if(requireParameterizedOutput){
            return super.visit(x);
         }
        ExportParameterVisitorUtils.exportParameter(this.parameters, x);
        return true;
    }

    public boolean visit(SQLBinaryOpExpr x) {
        if(requireParameterizedOutput){
            return super.visit(x);
         }
        ExportParameterVisitorUtils.exportParameter(this.parameters, x);
        return true;
    }

    @Override
    public boolean visit(MySqlFlushStatement x) {
        return true;
    }

    @Override
    public void endVisit(MySqlFlushStatement x) {

    }
}