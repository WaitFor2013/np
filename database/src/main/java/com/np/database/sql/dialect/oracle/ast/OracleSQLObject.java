
package com.np.database.sql.dialect.oracle.ast;

import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;

public interface OracleSQLObject extends SQLObject {

    void accept0(OracleASTVisitor visitor);
}
