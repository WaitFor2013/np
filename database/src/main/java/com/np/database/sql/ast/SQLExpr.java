
package com.np.database.sql.ast;

import java.util.List;

public interface SQLExpr extends SQLObject, Cloneable {
    SQLExpr     clone();
    SQLDataType computeDataType();
    List<SQLObject> getChildren();
}
