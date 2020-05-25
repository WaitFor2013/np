
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.ast.statement.SQLSelectOrderByItem;
import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.expr.SQLMethodInvokeExpr;
import com.np.database.sql.ast.statement.SQLSelectOrderByItem;
import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlTableIndex extends MySqlObjectImpl implements SQLTableElement {

    private SQLName name;
    private String                     indexType;
    private List<SQLSelectOrderByItem> columns = new ArrayList<SQLSelectOrderByItem>();

    public MySqlTableIndex(){

    }

    public SQLName getName() {
        return name;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public void setName(SQLName name) {
        this.name = name;
    }

    public List<SQLSelectOrderByItem> getColumns() {
        return columns;
    }
    
    public void addColumn(SQLSelectOrderByItem column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, columns);
        }
        visitor.endVisit(this);
    }

    public MySqlTableIndex clone() {
        MySqlTableIndex x = new MySqlTableIndex();
        if (name != null) {
            x.setName(name.clone());
        }
        x.indexType = indexType;
        for (SQLSelectOrderByItem column : columns) {
            SQLSelectOrderByItem c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        return x;
    }

    public boolean applyColumnRename(SQLName columnName, SQLName to) {
        for (SQLSelectOrderByItem orderByItem : columns) {
            SQLExpr expr = orderByItem.getExpr();
            if (expr instanceof SQLName
                    && NpSqlHelper.nameEquals((SQLName) expr, columnName)) {
                orderByItem.setExpr(to.clone());
                return true;
            }
        }
        return false;
    }

    public boolean applyDropColumn(SQLName columnName) {
        for (int i = columns.size() - 1; i >= 0; i--) {
            SQLExpr expr = columns.get(i).getExpr();
            if (expr instanceof SQLName
                    && NpSqlHelper.nameEquals((SQLName) expr, columnName)) {
                columns.remove(i);
                return true;
            }
            if (expr instanceof SQLMethodInvokeExpr
                    && NpSqlHelper.nameEquals(((SQLMethodInvokeExpr) expr).getMethodName(), columnName.getSimpleName())) {
                columns.remove(i);
                return true;
            }
        }
        return false;
    }
}
