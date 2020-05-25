
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLObject;

public interface SQLSelectQuery extends SQLObject {
    boolean isBracket();
    void setBracket(boolean bracket);

    SQLSelectQuery clone();
}
