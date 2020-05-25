
package com.np.database.sql.ast.statement;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLAlterDatabaseStatement extends SQLStatementImpl implements SQLAlterStatement {

    private SQLName name;

    private boolean upgradeDataDirectoryName;

    private SQLAlterCharacter character;
    
    public SQLAlterDatabaseStatement() {
        
    }
    
    public SQLAlterDatabaseStatement(String dbType) {
        this.setDbType(dbType);
    }

    public SQLName getName() {
        return name;
    }

    public void setName(SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }

    public SQLAlterCharacter getCharacter() {
        return character;
    }

    public void setCharacter(SQLAlterCharacter character) {
        if (character != null) {
            character.setParent(this);
        }
        this.character = character;
    }

    public boolean isUpgradeDataDirectoryName() {
        return upgradeDataDirectoryName;
    }

    public void setUpgradeDataDirectoryName(boolean upgradeDataDirectoryName) {
        this.upgradeDataDirectoryName = upgradeDataDirectoryName;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.<SQLObject>singletonList(name);
    }
}
