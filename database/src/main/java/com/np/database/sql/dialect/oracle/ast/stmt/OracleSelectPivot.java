
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleSelectPivot extends OracleSelectPivotBase {

    private boolean             xml;
    private final List<Item>    items    = new ArrayList<Item>();
    private final List<SQLExpr> pivotFor = new ArrayList<SQLExpr>();
    private final List<Item>    pivotIn  = new ArrayList<Item>();

    public OracleSelectPivot(){

    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
            acceptChild(visitor, this.pivotFor);
            acceptChild(visitor, this.pivotIn);
        }

        visitor.endVisit(this);
    }

    public List<Item> getPivotIn() {
        return this.pivotIn;
    }

    public List<SQLExpr> getPivotFor() {
        return this.pivotFor;
    }

    public boolean isXml() {
        return this.xml;
    }

    public List<Item> getItems() {
        return this.items;
    }
    
    public void addItem(Item item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }

    public void setXml(boolean xml) {
        this.xml = xml;
    }

    public static class Item extends OracleSQLObjectImpl {

        private String  alias;
        private SQLExpr expr;

        public Item(){

        }

        public String getAlias() {
            return this.alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public SQLExpr getExpr() {
            return this.expr;
        }

        public void setExpr(SQLExpr expr) {
            if (expr != null) {
                expr.setParent(this);
            }
            this.expr = expr;
        }

        public void accept0(OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, this.expr);
            }

            visitor.endVisit(this);
        }
    }
}
