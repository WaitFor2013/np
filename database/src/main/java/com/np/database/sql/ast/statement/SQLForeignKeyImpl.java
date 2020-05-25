
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLForeignKeyImpl extends SQLConstraintImpl implements SQLForeignKeyConstraint {
    private SQLExprTableSource referencedTable;
    private List<SQLName>      referencingColumns = new ArrayList<SQLName>();
    private List<SQLName>      referencedColumns  = new ArrayList<SQLName>();
    private boolean            onDeleteCascade    = false;
    private boolean            onDeleteSetNull    = false;

    public SQLForeignKeyImpl(){

    }

    @Override
    public List<SQLName> getReferencingColumns() {
        return referencingColumns;
    }

    @Override
    public SQLExprTableSource getReferencedTable() {
        return referencedTable;
    }

    @Override
    public SQLName getReferencedTableName() {
        if (referencedTable == null) {
            return null;
        }
        return referencedTable.getName();
    }

    @Override
    public void setReferencedTableName(SQLName value) {
        if (value == null) {
            this.referencedTable = null;
            return;
        }
        this.setReferencedTable(new SQLExprTableSource(value));
    }

    public void setReferencedTable(SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.referencedTable = x;
    }

    @Override
    public List<SQLName> getReferencedColumns() {
        return referencedColumns;
    }

    public boolean isOnDeleteCascade() {
        return onDeleteCascade;
    }

    public void setOnDeleteCascade(boolean onDeleteCascade) {
        this.onDeleteCascade = onDeleteCascade;
    }

    public boolean isOnDeleteSetNull() {
        return onDeleteSetNull;
    }

    public void setOnDeleteSetNull(boolean onDeleteSetNull) {
        this.onDeleteSetNull = onDeleteSetNull;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.getName());
            acceptChild(visitor, this.getReferencedTableName());
            acceptChild(visitor, this.getReferencingColumns());
            acceptChild(visitor, this.getReferencedColumns());
        }
        visitor.endVisit(this);        
    }

    public void cloneTo(SQLForeignKeyImpl x) {
        super.cloneTo(x);

        if (referencedTable != null) {
            x.setReferencedTable(referencedTable.clone());
        }

        for (SQLName column : referencingColumns) {
            SQLName columnClone = column.clone();
            columnClone.setParent(x);
            x.getReferencingColumns().add(columnClone);
        }

        for (SQLName column : referencedColumns) {
            SQLName columnClone = column.clone();
            columnClone.setParent(x);
            x.getReferencedColumns().add(columnClone);
        }
    }

    public SQLForeignKeyImpl clone() {
        SQLForeignKeyImpl x = new SQLForeignKeyImpl();
        cloneTo(x);
        return x;
    }

    public static enum Match {
        FULL("FULL"), PARTIAL("PARTIAL"), SIMPLE("SIMPLE");

        public final String name;
        public final String name_lcase;

        Match(String name){
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
    }

    public static enum On {
        DELETE("DELETE"), //
        UPDATE("UPDATE");

        public final String name;
        public final String name_lcase;

        On(String name){
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
    }

    public static enum Option {

        RESTRICT("RESTRICT"), CASCADE("CASCADE"), SET_NULL("SET NULL"), NO_ACTION("NO ACTION");

        public final String name;
        public final String name_lcase;

        Option(String name){
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }

        public String getText() {
            return name;
        }

    }
}
