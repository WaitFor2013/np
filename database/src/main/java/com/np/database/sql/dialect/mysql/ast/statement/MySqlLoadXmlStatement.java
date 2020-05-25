
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.expr.SQLLiteralExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.expr.SQLLiteralExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlLoadXmlStatement extends MySqlStatementImpl {

    private boolean             lowPriority = false;
    private boolean             concurrent  = false;
    private boolean             local       = false;

    private SQLLiteralExpr fileName;

    private boolean             replicate   = false;
    private boolean             ignore      = false;

    private SQLName tableName;

    private String              charset;

    private SQLExpr rowsIdentifiedBy;

    private SQLExpr             ignoreLinesNumber;

    private final List<SQLExpr> setList     = new ArrayList<SQLExpr>();

    public SQLExpr getRowsIdentifiedBy() {
        return rowsIdentifiedBy;
    }

    public void setRowsIdentifiedBy(SQLExpr rowsIdentifiedBy) {
        this.rowsIdentifiedBy = rowsIdentifiedBy;
    }

    public boolean isLowPriority() {
        return lowPriority;
    }

    public void setLowPriority(boolean lowPriority) {
        this.lowPriority = lowPriority;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public SQLLiteralExpr getFileName() {
        return fileName;
    }

    public void setFileName(SQLLiteralExpr fileName) {
        this.fileName = fileName;
    }

    public boolean isReplicate() {
        return replicate;
    }

    public void setReplicate(boolean replicate) {
        this.replicate = replicate;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public SQLName getTableName() {
        return tableName;
    }

    public void setTableName(SQLName tableName) {
        this.tableName = tableName;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public SQLExpr getIgnoreLinesNumber() {
        return ignoreLinesNumber;
    }

    public void setIgnoreLinesNumber(SQLExpr ignoreLinesNumber) {
        this.ignoreLinesNumber = ignoreLinesNumber;
    }

    public List<SQLExpr> getSetList() {
        return setList;
    }

    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, fileName);
            acceptChild(visitor, tableName);
            acceptChild(visitor, rowsIdentifiedBy);
            // acceptChild(visitor, columnsTerminatedBy);
            // acceptChild(visitor, columnsEnclosedBy);
            // acceptChild(visitor, columnsEscaped);
            // acceptChild(visitor, linesStartingBy);
            // acceptChild(visitor, linesTerminatedBy);
            acceptChild(visitor, ignoreLinesNumber);
            acceptChild(visitor, setList);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        if (fileName != null) {
            children.add(fileName);
        }
        if (tableName != null) {
            children.add(tableName);
        }
        if (rowsIdentifiedBy != null) {
            children.add(rowsIdentifiedBy);
        }
        if (ignoreLinesNumber != null) {
            children.add(ignoreLinesNumber);
        }
        children.addAll(this.setList);
        return children;
    }
}
