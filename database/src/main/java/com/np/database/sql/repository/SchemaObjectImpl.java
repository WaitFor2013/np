
package com.np.database.sql.repository;

import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLColumnDefinition;
import com.np.database.sql.ast.statement.SQLCreateTableStatement;
import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.ast.statement.SQLUniqueConstraint;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLColumnDefinition;
import com.np.database.sql.ast.statement.SQLCreateTableStatement;
import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.ast.statement.SQLUniqueConstraint;
import com.np.database.sql.util.FnvHash;

/**
 * Created by wenshao on 08/06/2017.
 */
public class SchemaObjectImpl implements SchemaObject {
    private final String name;
    private final long   hashCode64;

    private final SchemaObjectType type;
    private SQLStatement statement;

    public long rowCount = -1;

    public SchemaObjectImpl(String name, SchemaObjectType type) {
        this(name, type, null);
    }

    public SchemaObjectImpl(String name, SchemaObjectType type, SQLStatement statement) {
        this.name = name;
        this.type = type;
        this.statement = statement;

        this.hashCode64 = FnvHash.hashCode64(name);
    }

    public long nameHashCode64() {
        return hashCode64;
    }

    public static enum Type {
        Sequence, Table, View, Index, Function
    }

    public SQLStatement getStatement() {
        return statement;
    }

    public SQLColumnDefinition findColumn(String columName) {
        long hash = FnvHash.hashCode64(columName);
        return findColumn(hash);
    }

    public SQLColumnDefinition findColumn(long columNameHash) {
        if (statement == null) {
            return null;
        }

        if (statement instanceof SQLCreateTableStatement) {
            return ((SQLCreateTableStatement) statement).findColumn(columNameHash);
        }

        return null;
    }

    public boolean matchIndex(String columnName) {
        if (statement == null) {
            return false;
        }

        if (statement instanceof SQLCreateTableStatement) {
            SQLTableElement index = ((SQLCreateTableStatement) statement).findIndex(columnName);
            return index != null;
        }

        return false;
    }

    public boolean matchKey(String columnName) {
        if (statement == null) {
            return false;
        }

        if (statement instanceof SQLCreateTableStatement) {
            SQLTableElement index = ((SQLCreateTableStatement) statement).findIndex(columnName);
            return index instanceof SQLUniqueConstraint;
        }

        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SchemaObjectType getType() {
        return type;
    }

    @Override
    public long getRowCount() {
        return rowCount;
    }
}
