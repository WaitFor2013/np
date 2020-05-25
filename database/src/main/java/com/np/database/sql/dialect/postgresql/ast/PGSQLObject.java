
package com.np.database.sql.dialect.postgresql.ast;

import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;

public interface PGSQLObject extends SQLObject {

    void accept0(PGASTVisitor visitor);
}
