
package com.np.database.sql.dialect.mysql.ast.statement;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLExplainStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.statement.SQLExplainStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.visitor.SQLASTVisitor;

public class MySqlExplainStatement extends SQLExplainStatement implements MySqlStatement {
    private boolean describe;
    private SQLName tableName;
    private SQLName columnName;
    private SQLExpr wild;
    private String  format;
    private SQLExpr connectionId;

    public MySqlExplainStatement() {
        super (JdbcConstants.MYSQL);
    }

       public MySqlExplainStatement(String dbType) {
        super (dbType);
    }


    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            // tbl_name [col_name | wild]
            if (tableName != null) {
                acceptChild(visitor, tableName);
                if (columnName != null) {
                    acceptChild(visitor, columnName);
                } else if (wild != null) {
                    acceptChild(visitor, wild);
                }
            } else {
                // {explainable_stmt | FOR CONNECTION connection_id}
                if (connectionId != null) {
                    acceptChild(visitor, connectionId);
                } else {
                    acceptChild(visitor, statement);
                }
            }
        }

        visitor.endVisit(this);
    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((MySqlASTVisitor) visitor);
    }

    public String toString() {
        return NpSqlHelper.toMySqlString(this);
    }

    public boolean isDescribe() {
        return describe;
    }

    public void setDescribe(boolean describe) {
        this.describe = describe;
    }

    public SQLName getTableName() {
        return tableName;
    }

    public void setTableName(SQLName tableName) {
        this.tableName = tableName;
    }

    public SQLName getColumnName() {
        return columnName;
    }

    public void setColumnName(SQLName columnName) {
        this.columnName = columnName;
    }

    public SQLExpr getWild() {
        return wild;
    }

    public void setWild(SQLExpr wild) {
        this.wild = wild;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public SQLExpr getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(SQLExpr connectionId) {
        this.connectionId = connectionId;
    }

}
