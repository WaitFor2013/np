
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;

public interface SQLConstraint extends SQLObject {

    SQLName getName();

    void setName(SQLName value);

    SQLExpr getComment();
    void setComment(SQLExpr x);

    void simplify();
}
