
package com.np.database.sql.ast;

/**
 * Created by wenshao on 06/06/2017.
 */
public interface SQLReplaceable {
    boolean replace(SQLExpr expr, SQLExpr target);
}
