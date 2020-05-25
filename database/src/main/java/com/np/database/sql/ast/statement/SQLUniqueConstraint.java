
package com.np.database.sql.ast.statement;

import java.util.List;

public interface SQLUniqueConstraint extends SQLConstraint {

    List<SQLSelectOrderByItem> getColumns();

    boolean containsColumn(String column);
    boolean containsColumn(long columnNameHash);
}
