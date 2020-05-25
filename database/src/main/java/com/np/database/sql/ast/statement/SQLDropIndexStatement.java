
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLDropIndexStatement extends SQLStatementImpl implements SQLDropStatement {

    private SQLName            indexName;
    private SQLExprTableSource tableName;

    private SQLExpr            algorithm;
    private SQLExpr            lockOption;
    
    public SQLDropIndexStatement() {
        
    }
    
    public SQLDropIndexStatement(String dbType) {
        super (dbType);
    }

    public SQLName getIndexName() {
        return indexName;
    }

    public void setIndexName(SQLName indexName) {
        this.indexName = indexName;
    }

    public SQLExprTableSource getTableName() {
        return tableName;
    }

    public void setTableName(SQLName tableName) {
        this.setTableName(new SQLExprTableSource(tableName));
    }

    public void setTableName(SQLExprTableSource tableName) {
        this.tableName = tableName;
    }

    public SQLExpr getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.algorithm = x;
    }

    public SQLExpr getLockOption() {
        return lockOption;
    }

    public void setLockOption(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.lockOption = x;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, indexName);
            acceptChild(visitor, tableName);
            acceptChild(visitor, algorithm);
            acceptChild(visitor, lockOption);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (indexName != null) {
            children.add(indexName);
        }
        if (tableName != null) {
            children.add(tableName);
        }
        return children;
    }
}
