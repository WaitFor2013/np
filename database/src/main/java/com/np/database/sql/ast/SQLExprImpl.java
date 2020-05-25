
package com.np.database.sql.ast;

import java.util.Collections;
import java.util.List;

public abstract class SQLExprImpl extends SQLObjectImpl implements SQLExpr {

    public SQLExprImpl(){

    }

    public abstract boolean equals(Object o);

    public abstract int hashCode();

    public SQLExpr clone() {
        throw new UnsupportedOperationException();
    }

    public SQLDataType computeDataType() {
        return null;
    }


    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
