
package com.np.database.sql.repository;

import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLColumnDefinition;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLColumnDefinition;

/**
 * Created by wenshao on 03/06/2017.
 */
public interface SchemaObject {

    SQLStatement getStatement();

    SQLColumnDefinition findColumn(String columName);
    SQLColumnDefinition findColumn(long columNameHash);

    boolean matchIndex(String columnName);

    boolean matchKey(String columnName);

    SchemaObjectType getType();

    String getName();
    long nameHashCode64();

    long getRowCount();
}
