
package com.np.database.sql.ast.statement;

import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLHint;
import com.np.database.sql.ast.SQLObject;

public interface SQLTableSource extends SQLObject {

    String getAlias();
    long aliasHashCode64();

    void setAlias(String alias);
    
    List<SQLHint> getHints();

    SQLTableSource clone();

    String computeAlias();
    boolean containsAlias(String alias);

    SQLExpr getFlashback();
    void setFlashback(SQLExpr flashback);

    SQLObject resolveColum(long columnNameHash);

    SQLColumnDefinition findColumn(String columnName);
    SQLColumnDefinition findColumn(long columnNameHash);

    SQLTableSource findTableSourceWithColumn(String columnName);
    SQLTableSource findTableSourceWithColumn(long columnName_hash);

    SQLTableSource findTableSource(String alias);
    SQLTableSource findTableSource(long alias_hash);
}
