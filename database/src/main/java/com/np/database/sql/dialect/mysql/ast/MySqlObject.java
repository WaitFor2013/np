
package com.np.database.sql.dialect.mysql.ast;

import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;


public interface MySqlObject extends SQLObject {
    void accept0(MySqlASTVisitor visitor);
}
