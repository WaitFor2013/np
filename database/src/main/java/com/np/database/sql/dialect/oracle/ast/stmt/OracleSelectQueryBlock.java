
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLBinaryOperator;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLIntegerExpr;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObject;
import com.np.database.sql.dialect.oracle.ast.clause.ModelClause;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLBinaryOperator;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLIntegerExpr;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.dialect.oracle.ast.clause.ModelClause;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleSelectQueryBlock extends SQLSelectQueryBlock implements OracleSQLObject {

    private ModelClause modelClause;
    private boolean                    skipLocked  = false;

    public OracleSelectQueryBlock clone() {
        OracleSelectQueryBlock x = new OracleSelectQueryBlock();

        super.cloneTo(x);

        if (hints != null) {
            for (SQLCommentHint hint : hints) {
                SQLCommentHint hint1 = hint.clone();
                hint1.setParent(x);
                x.getHints().add(hint1);
            }
        }

        if (modelClause != null) {
            x.setModelClause(modelClause.clone());
        }

        if (forUpdateOf != null) {
            for (SQLExpr item : forUpdateOf) {
                SQLExpr item1 = item.clone();
                item1.setParent(x);
                forUpdateOf.add(item1);
            }
        }

        x.skipLocked = skipLocked;

        return x;
    }

    public OracleSelectQueryBlock(){
        dbType = JdbcConstants.ORACLE;
    }

    public ModelClause getModelClause() {
        return modelClause;
    }

    public void setModelClause(ModelClause modelClause) {
        this.modelClause = modelClause;
    }

    public boolean isSkipLocked() {
        return skipLocked;
    }

    public void setSkipLocked(boolean skipLocked) {
        this.skipLocked = skipLocked;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            accept0((OracleASTVisitor) visitor);
            return;
        }

        super.accept0(visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.hints);
            acceptChild(visitor, this.selectList);
            acceptChild(visitor, this.into);
            acceptChild(visitor, this.from);
            acceptChild(visitor, this.where);
            acceptChild(visitor, this.startWith);
            acceptChild(visitor, this.connectBy);
            acceptChild(visitor, this.groupBy);
            acceptChild(visitor, this.orderBy);
            acceptChild(visitor, this.waitTime);
            acceptChild(visitor, this.limit);
            acceptChild(visitor, this.modelClause);
            acceptChild(visitor, this.forUpdateOf);
        }
        visitor.endVisit(this);
    }
    
    public String toString() {
        return NpSqlHelper.toOracleString(this);
    }

    public void limit(int rowCount, int offset) {
        if (offset <= 0) {
            SQLExpr rowCountExpr = new SQLIntegerExpr(rowCount);
            SQLExpr newCondition = NpSqlHelper.buildCondition(SQLBinaryOperator.BooleanAnd, rowCountExpr, false,
                    where);
            setWhere(newCondition);
        } else {
            throw new UnsupportedOperationException("not support offset");
        }
    }

    public void setFrom(String tableName) {
        SQLExprTableSource from;
        if (tableName == null || tableName.length() == 0) {
            from = null;
        } else {
            from = new OracleSelectTableReference(new SQLIdentifierExpr(tableName));
        }
        this.setFrom(from);
    }
}
