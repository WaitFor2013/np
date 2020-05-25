
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.statement.SQLTableSource;
import com.np.database.sql.ast.statement.SQLTableSource;

public interface OracleSelectTableSource extends SQLTableSource {

    OracleSelectPivotBase getPivot();

    void setPivot(OracleSelectPivotBase pivot);
}
