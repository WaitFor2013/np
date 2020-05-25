
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

/**
 * Created by wenshao on 20/05/2017.
 */
public class OracleSupplementalLogGrp extends OracleSQLObjectImpl implements SQLTableElement {
    private SQLName group;
    private List<SQLName> columns = new ArrayList<SQLName>();
    private boolean always = false;

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, group);
            acceptChild(visitor, columns);
        }
        visitor.endVisit(this);
    }

    public SQLName getGroup() {
        return group;
    }

    public void setGroup(SQLName group) {
        if (group != null) {
            group.setParent(this);
        }
        this.group = group;
    }

    public List<SQLName> getColumns() {
        return columns;
    }

    public void addColumn(SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }

    public boolean isAlways() {
        return always;
    }

    public void setAlways(boolean always) {
        this.always = always;
    }

    public OracleSupplementalLogGrp clone() {
        OracleSupplementalLogGrp x = new OracleSupplementalLogGrp();
        if (group != null) {
            x.setGroup(group.clone());
        }
        for (SQLName column : columns) {
            SQLName c2 = column.clone();
            c2.setParent(x);
            x.columns.add(c2);
        }
        x.always = always;
        return x;
    }
}
