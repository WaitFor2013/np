
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLAlterStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLAlterStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlAlterTablespaceStatement extends MySqlStatementImpl implements SQLAlterStatement {
    private SQLName name;

    private SQLExpr addDataFile;
    private SQLExpr dropDataFile;
    private SQLExpr initialSize;
    private boolean wait;
    private SQLExpr engine;

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, name);
            acceptChild(visitor, addDataFile);
            acceptChild(visitor, initialSize);
            acceptChild(visitor, engine);
        }
        visitor.endVisit(this);
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

    public SQLExpr getAddDataFile() {
        return addDataFile;
    }

    public void setAddDataFile(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.addDataFile = addDataFile;
    }

    public SQLExpr getDropDataFile() {
        return dropDataFile;
    }

    public void setDropDataFile(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.dropDataFile = dropDataFile;
    }

    public SQLExpr getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.initialSize = x;
    }

    public boolean isWait() {
        return wait;
    }

    public void setWait(boolean wait) {
        this.wait = wait;
    }

    public SQLExpr getEngine() {
        return engine;
    }

    public void setEngine(SQLExpr engine) {
        if (engine != null) {
            engine.setParent(this);
        }
        this.engine = engine;
    }
}