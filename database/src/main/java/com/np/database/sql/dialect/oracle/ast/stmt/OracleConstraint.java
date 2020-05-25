
package com.np.database.sql.dialect.oracle.ast.stmt;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLConstraint;
import com.np.database.sql.ast.statement.SQLTableElement;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObject;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLConstraint;
import com.np.database.sql.ast.statement.SQLTableElement;

public interface OracleConstraint extends OracleSQLObject, SQLConstraint, SQLTableElement {

    SQLName getExceptionsInto();

    void setExceptionsInto(SQLName exceptionsInto);

    Boolean getDeferrable();

    void setDeferrable(Boolean enable);

    Boolean getEnable();

    void setEnable(Boolean enable);

    Boolean getValidate();
    void setValidate(Boolean validate);

    Initially getInitially();

    void setInitially(Initially value);

    OracleUsingIndexClause getUsing();

    void setUsing(OracleUsingIndexClause using);

    public static enum Initially {
        DEFERRED, IMMEDIATE
    }

    OracleConstraint clone();
}
