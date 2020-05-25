
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLDeclareItem;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLDeclareStatement extends SQLStatementImpl {

    protected List<SQLDeclareItem> items = new ArrayList<SQLDeclareItem>();
    
    public SQLDeclareStatement() {

    }

    public SQLDeclareStatement(SQLName name, SQLDataType dataType) {
        this.addItem(new SQLDeclareItem(name, dataType));
    }

    public SQLDeclareStatement(SQLName name, SQLDataType dataType, SQLExpr value) {
        this.addItem(new SQLDeclareItem(name, dataType, value));
    }

    @Override
    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, items);
        }
        visitor.endVisit(this);
    }

    public List<SQLDeclareItem> getItems() {
        return items;
    }

    public void addItem(SQLDeclareItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
}
