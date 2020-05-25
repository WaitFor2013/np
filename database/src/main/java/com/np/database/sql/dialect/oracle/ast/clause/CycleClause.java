
package com.np.database.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObjectImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public class CycleClause extends OracleSQLObjectImpl {

    private final List<SQLExpr> aliases = new ArrayList<SQLExpr>();
    private SQLExpr             mark;
    private SQLExpr             value;
    private SQLExpr             defaultValue;

    public SQLExpr getMark() {
        return mark;
    }

    public void setMark(SQLExpr mark) {
        if (mark != null) {
            mark.setParent(this);
        }
        this.mark = mark;
    }

    public SQLExpr getValue() {
        return value;
    }

    public void setValue(SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.value = value;
    }

    public SQLExpr getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(SQLExpr defaultValue) {
        if (defaultValue != null) {
            defaultValue.setParent(this);
        }
        this.defaultValue = defaultValue;
    }

    public List<SQLExpr> getAliases() {
        return aliases;
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, aliases);
            acceptChild(visitor, mark);
            acceptChild(visitor, value);
            acceptChild(visitor, defaultValue);
        }
        visitor.endVisit(this);
    }

    public CycleClause clone() {
        CycleClause x = new CycleClause();

        for (SQLExpr alias : aliases) {
            SQLExpr alias2 = alias.clone();
            alias2.setParent(x);
            x.aliases.add(alias2);
        }

        if (mark != null) {
            setMark(mark.clone());
        }

        if (value != null) {
            setValue(value.clone());
        }

        if (defaultValue != null) {
           setDefaultValue(defaultValue.clone());
        }

        return x;
    }
}
