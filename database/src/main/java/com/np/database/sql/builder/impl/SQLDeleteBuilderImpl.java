
package com.np.database.sql.builder.impl;

import java.util.List;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.expr.SQLBinaryOperator;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.statement.SQLDeleteStatement;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.builder.SQLDeleteBuilder;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.np.database.sql.dialect.oracle.ast.stmt.OracleDeleteStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.np.database.sql.util.JdbcConstants;

public class SQLDeleteBuilderImpl implements SQLDeleteBuilder {

    private SQLDeleteStatement stmt;
    private String             dbType;

    public SQLDeleteBuilderImpl(String dbType){
        this.dbType = dbType;
    }
    
    public SQLDeleteBuilderImpl(String sql, String dbType){
        List<SQLStatement> stmtList = NpSqlHelper.parseStatements(sql, dbType);

        if (stmtList.size() == 0) {
            throw new IllegalArgumentException("not support empty-statement :" + sql);
        }

        if (stmtList.size() > 1) {
            throw new IllegalArgumentException("not support multi-statement :" + sql);
        }

        SQLDeleteStatement stmt = (SQLDeleteStatement) stmtList.get(0);
        this.stmt = stmt;
        this.dbType = dbType;
    }

    public SQLDeleteBuilderImpl(SQLDeleteStatement stmt, String dbType){
        this.stmt = stmt;
        this.dbType = dbType;
    }

    @Override
    public SQLDeleteBuilderImpl limit(int rowCount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLDeleteBuilderImpl limit(int rowCount, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLDeleteBuilder from(String table) {
        return from(table, null);
    }

    @Override
    public SQLDeleteBuilder from(String table, String alias) {
        SQLDeleteStatement delete = getSQLDeleteStatement();
        SQLExprTableSource from = new SQLExprTableSource(new SQLIdentifierExpr(table), alias);
        delete.setTableSource(from);
        return this;
    }

    @Override
    public SQLDeleteBuilder where(String expr) {
        SQLDeleteStatement delete = getSQLDeleteStatement();

        SQLExpr exprObj = NpSqlHelper.toSQLExpr(expr, dbType);
        delete.setWhere(exprObj);

        return this;
    }

    @Override
    public SQLDeleteBuilder whereAnd(String expr) {
        SQLDeleteStatement delete = getSQLDeleteStatement();

        SQLExpr exprObj = NpSqlHelper.toSQLExpr(expr, dbType);
        SQLExpr newCondition = NpSqlHelper.buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, delete.getWhere());
        delete.setWhere(newCondition);

        return this;
    }

    @Override
    public SQLDeleteBuilder whereOr(String expr) {
        SQLDeleteStatement delete = getSQLDeleteStatement();

        SQLExpr exprObj = NpSqlHelper.toSQLExpr(expr, dbType);
        SQLExpr newCondition = NpSqlHelper.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, delete.getWhere());
        delete.setWhere(newCondition);

        return this;
    }

    public SQLDeleteStatement getSQLDeleteStatement() {
        if (stmt == null) {
            stmt = createSQLDeleteStatement();
        }
        return stmt;
    }

    public SQLDeleteStatement createSQLDeleteStatement() {
        if (JdbcConstants.ORACLE.equals(dbType)) {
            return new OracleDeleteStatement();    
        }
        
        if (JdbcConstants.MYSQL.equals(dbType)) {
            return new MySqlDeleteStatement();    
        }
        
        if (JdbcConstants.POSTGRESQL.equals(dbType)) {
            return new PGDeleteStatement();    
        }
        
        return new SQLDeleteStatement();
    }

    public String toString() {
        return NpSqlHelper.toSQLString(stmt, dbType);
    }
}
