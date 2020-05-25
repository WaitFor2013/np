
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLObject;

public interface SQLTableElement extends SQLObject {
    SQLTableElement clone();
}
