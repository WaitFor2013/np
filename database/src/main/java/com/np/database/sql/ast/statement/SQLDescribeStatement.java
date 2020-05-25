
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLDescribeStatement extends SQLStatementImpl {

    protected SQLName object;

    protected SQLName column;

    // for odps
    protected SQLObjectType objectType;
    protected List<SQLExpr> partition = new ArrayList<SQLExpr>();

    public SQLName getObject() {
        return object;
    }

    public void setObject(SQLName object) {
        this.object = object;
    }

    public SQLName getColumn() {
        return column;
    }

    public void setColumn(SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.column = column;
    }

    public List<SQLExpr> getPartition() {
        return partition;
    }

    public void setPartition(List<SQLExpr> partition) {
        this.partition = partition;
    }

    public SQLObjectType getObjectType() {
        return objectType;
    }

    public void setObjectType(SQLObjectType objectType) {
        this.objectType = objectType;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, object);
            acceptChild(visitor, column);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        return Arrays.<SQLObject>asList(this.object, column);

    }
}
