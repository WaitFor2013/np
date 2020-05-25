
package com.np.database.sql.builder.impl;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.expr.SQLBinaryOperator;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.ast.statement.SQLUpdateSetItem;
import com.np.database.sql.ast.statement.SQLUpdateStatement;
import com.np.database.sql.builder.SQLUpdateBuilder;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.np.database.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.builder.SQLUpdateBuilder;

import java.util.List;
import java.util.Map;

public class SQLUpdateBuilderImpl extends SQLBuilderImpl implements SQLUpdateBuilder {

    private SQLUpdateStatement stmt;
    private String             dbType;

    public SQLUpdateBuilderImpl(String dbType){
        this.dbType = dbType;
    }
    
    public SQLUpdateBuilderImpl(String sql, String dbType){
        List<SQLStatement> stmtList = NpSqlHelper.parseStatements(sql, dbType);

        if (stmtList.size() == 0) {
            throw new IllegalArgumentException("not support empty-statement :" + sql);
        }

        if (stmtList.size() > 1) {
            throw new IllegalArgumentException("not support multi-statement :" + sql);
        }

        SQLUpdateStatement stmt = (SQLUpdateStatement) stmtList.get(0);
        this.stmt = stmt;
        this.dbType = dbType;
    }

    public SQLUpdateBuilderImpl(SQLUpdateStatement stmt, String dbType){
        this.stmt = stmt;
        this.dbType = dbType;
    }

    @Override
    public SQLUpdateBuilderImpl limit(int rowCount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLUpdateBuilderImpl limit(int rowCount, int offset) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SQLUpdateBuilderImpl from(String table) {
        return from(table, null);
    }

    @Override
    public SQLUpdateBuilderImpl from(String table, String alias) {
        SQLUpdateStatement update = getSQLUpdateStatement();
        SQLExprTableSource from = new SQLExprTableSource(new SQLIdentifierExpr(table), alias);
        update.setTableSource(from);
        return this;
    }

    @Override
    public SQLUpdateBuilderImpl where(String expr) {
        SQLUpdateStatement update = getSQLUpdateStatement();

        SQLExpr exprObj = NpSqlHelper.toSQLExpr(expr, dbType);
        update.setWhere(exprObj);

        return this;
    }

    @Override
    public SQLUpdateBuilderImpl whereAnd(String expr) {
        SQLUpdateStatement update = getSQLUpdateStatement();

        SQLExpr exprObj = NpSqlHelper.toSQLExpr(expr, dbType);
        SQLExpr newCondition = NpSqlHelper.buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, update.getWhere());
        update.setWhere(newCondition);

        return this;
    }

    @Override
    public SQLUpdateBuilderImpl whereOr(String expr) {
        SQLUpdateStatement update = getSQLUpdateStatement();

        SQLExpr exprObj = NpSqlHelper.toSQLExpr(expr, dbType);
        SQLExpr newCondition = NpSqlHelper.buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, update.getWhere());
        update.setWhere(newCondition);

        return this;
    }

    public SQLUpdateBuilderImpl set(String... items) {
        SQLUpdateStatement update = getSQLUpdateStatement();
        for (String item : items) {
            SQLUpdateSetItem updateSetItem = NpSqlHelper.toUpdateSetItem(item, dbType);
            update.addItem(updateSetItem);
        }
        
        return this;
    }
    
    public SQLUpdateBuilderImpl setValue(Map<String, Object> values) {
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            setValue(entry.getKey(), entry.getValue());
        }
        
        return this;
    }
    
    public SQLUpdateBuilderImpl setValue(String column, Object value) {
        SQLUpdateStatement update = getSQLUpdateStatement();
        
        SQLExpr columnExpr = NpSqlHelper.toSQLExpr(column, dbType);
        SQLExpr valueExpr = toSQLExpr(value, dbType);
        
        SQLUpdateSetItem item = new SQLUpdateSetItem();
        item.setColumn(columnExpr);
        item.setValue(valueExpr);
        update.addItem(item);
        
        return this;
    }

    public SQLUpdateStatement getSQLUpdateStatement() {
        if (stmt == null) {
            stmt = createSQLUpdateStatement();
        }
        return stmt;
    }

    public SQLUpdateStatement createSQLUpdateStatement() {
        if (JdbcConstants.MYSQL.equals(dbType)) {
            return new MySqlUpdateStatement();    
        }
        
        if (JdbcConstants.ORACLE.equals(dbType)) {
            return new OracleUpdateStatement();    
        }
        
        if (JdbcConstants.POSTGRESQL.equals(dbType)) {
            return new PGUpdateStatement();    
        }
        
        return new SQLUpdateStatement();
    }
    
    public String toString() {
        return NpSqlHelper.toSQLString(stmt, dbType);
    }
}
