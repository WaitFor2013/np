
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.ast.stmt.OracleSelectPivot.Item;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class OracleSelectUnPivot extends OracleSelectPivotBase {

    private NullsIncludeType                   nullsIncludeType;
    private final List<SQLExpr>                items   = new ArrayList<SQLExpr>();

    private final List<OracleSelectPivot.Item> pivotIn = new ArrayList<OracleSelectPivot.Item>();

    public OracleSelectUnPivot(){

    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.items);
            acceptChild(visitor, this.pivotIn);
        }
        visitor.endVisit(this);
    }

    public List<OracleSelectPivot.Item> getPivotIn() {
        return this.pivotIn;
    }

    public List<SQLExpr> getItems() {
        return this.items;
    }
    
    public void addItem(SQLExpr item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }

    public NullsIncludeType getNullsIncludeType() {
        return this.nullsIncludeType;
    }

    public void setNullsIncludeType(NullsIncludeType nullsIncludeType) {
        this.nullsIncludeType = nullsIncludeType;
    }

    public static enum NullsIncludeType {
        INCLUDE_NULLS, EXCLUDE_NULLS;

        public static String toString(NullsIncludeType type, boolean ucase) {
            if (INCLUDE_NULLS.equals(type)) {
                return ucase ? "INCLUDE NULLS" : "include nulls";
            }
            if (EXCLUDE_NULLS.equals(type)) {
                return ucase ? "EXCLUDE NULLS" : "exclude nulls";
            }

            throw new IllegalArgumentException();
        }
    }
}
