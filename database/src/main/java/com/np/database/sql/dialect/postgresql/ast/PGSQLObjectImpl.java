
package com.np.database.sql.dialect.postgresql.ast;

import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public abstract class PGSQLObjectImpl extends SQLObjectImpl implements PGSQLObject {

    public PGSQLObjectImpl(){

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((PGASTVisitor) visitor);
    }

    public abstract void accept0(PGASTVisitor visitor);
}
