
package com.np.database.sql;


import com.np.database.exception.NpDbException;
import com.np.database.sql.ast.expr.SQLAggregateExpr;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import com.np.database.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.ast.*;
import com.np.database.sql.ast.expr.*;
import com.np.database.sql.ast.statement.*;
import com.np.database.sql.util.JdbcConstants;

import java.util.List;

public class PagerUtils {

    public static String count(String sql, String dbType) {

        List<SQLStatement> stmtList = NpSqlHelper.parseStatements(sql, dbType);

        if (stmtList.size() != 1) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }

        SQLStatement stmt = stmtList.get(0);

        if (!(stmt instanceof SQLSelectStatement)) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }

        SQLSelectStatement selectStmt = (SQLSelectStatement) stmt;
        return count(selectStmt.getSelect(), dbType);
    }

    public static String limit(String sql, String dbType, int offset, int count) {
        List<SQLStatement> stmtList = NpSqlHelper.parseStatements(sql, dbType);

        if (stmtList.size() != 1) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }

        SQLStatement stmt = stmtList.get(0);

        if (!(stmt instanceof SQLSelectStatement)) {
            throw new IllegalArgumentException("sql not support count : " + sql);
        }

        SQLSelectStatement selectStmt = (SQLSelectStatement) stmt;

        return limit(selectStmt.getSelect(), dbType, offset, count);
    }

    public static String limit(SQLSelect select, String dbType, int offset, int count) {
        limit(select, dbType, offset, count, false);

        return NpSqlHelper.toSQLString(select, dbType);
    }

    public static boolean limit(SQLSelect select, String dbType, int offset, int count, boolean check) {
        if (JdbcConstants.ORACLE.equals(dbType)) {
            return limitOracle(select, dbType, offset, count, check);
        }

        return limitQueryBlock(select, dbType, offset, count, check);
    }

    private static boolean limitQueryBlock(SQLSelect select, String dbType, int offset, int count, boolean check) {
        SQLSelectQuery query = select.getQuery();
        if (query instanceof SQLUnionQuery) {
            SQLUnionQuery union = (SQLUnionQuery) query;
            return limitUnion(union, dbType, offset, count, check);
        }

        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) query;
        if (JdbcConstants.MYSQL.equals(dbType) || //
            JdbcConstants.MARIADB.equals(dbType) || //
            JdbcConstants.H2.equals(dbType)) {
            return limitMySqlQueryBlock(queryBlock, dbType, offset, count, check);
        }

        if (JdbcConstants.POSTGRESQL.equals(dbType)) {
            return limitPostgreSQLQueryBlock((PGSelectQueryBlock) queryBlock, dbType, offset, count, check);
        }
        throw new UnsupportedOperationException();
    }

    private static boolean limitPostgreSQLQueryBlock(PGSelectQueryBlock queryBlock, String dbType, int offset, int count, boolean check) {
        SQLLimit limit = queryBlock.getLimit();
        if (limit != null) {
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            }

            if (check && limit.getRowCount() instanceof SQLNumericLiteralExpr) {
                int rowCount = ((SQLNumericLiteralExpr) limit.getRowCount()).getNumber().intValue();
                if (rowCount <= count && offset <= 0) {
                    return false;
                }
            }

            limit.setRowCount(new SQLIntegerExpr(count));
        }

        limit = new SQLLimit();
        if (offset > 0) {
            limit.setOffset(new SQLIntegerExpr(offset));
        }
        limit.setRowCount(new SQLIntegerExpr(count));
        queryBlock.setLimit(limit);
        return true;
    }

    private static boolean limitOracle(SQLSelect select, String dbType, int offset, int count, boolean check) {
        SQLSelectQuery query = select.getQuery();

        if (query instanceof SQLSelectQueryBlock) {
            OracleSelectQueryBlock queryBlock = (OracleSelectQueryBlock) query;
            SQLOrderBy orderBy = select.getOrderBy();
            if (orderBy == null && queryBlock.getOrderBy() != null) {
                orderBy = queryBlock.getOrderBy();
            }

            if (queryBlock.getGroupBy() == null
                    && orderBy == null && offset <= 0) {

                SQLExpr where = queryBlock.getWhere();
                if (check && where instanceof SQLBinaryOpExpr) {
                    SQLBinaryOpExpr binaryOpWhere = (SQLBinaryOpExpr) where;
                    if (binaryOpWhere.getOperator() == com.np.database.sql.ast.expr.SQLBinaryOperator.LessThanOrEqual) {
                        SQLExpr left = binaryOpWhere.getLeft();
                        SQLExpr right = binaryOpWhere.getRight();
                        if (left instanceof SQLIdentifierExpr
                                && ((SQLIdentifierExpr) left).getName().equalsIgnoreCase("ROWNUM")
                                && right instanceof SQLNumericLiteralExpr) {
                            int rowCount = ((SQLNumericLiteralExpr) right).getNumber().intValue();
                            if (rowCount <= count) {
                                return false;
                            }
                        }
                    }
                }

                SQLExpr condition = new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), //
                                                        com.np.database.sql.ast.expr.SQLBinaryOperator.LessThanOrEqual, //
                                                        new SQLNumberExpr(count), //
                                                        JdbcConstants.ORACLE);
                if (queryBlock.getWhere() == null) {
                    queryBlock.setWhere(condition);
                } else {
                    queryBlock.setWhere(new SQLBinaryOpExpr(queryBlock.getWhere(), //
                                                            com.np.database.sql.ast.expr.SQLBinaryOperator.BooleanAnd, //
                                                            condition, //
                                                            JdbcConstants.ORACLE));
                }

                return true;
            }
        }

        OracleSelectQueryBlock countQueryBlock = new OracleSelectQueryBlock();
        countQueryBlock.getSelectList().add(new SQLSelectItem(new SQLPropertyExpr(new SQLIdentifierExpr("XX"), "*")));
        countQueryBlock.getSelectList().add(new SQLSelectItem(new SQLIdentifierExpr("ROWNUM"), "RN"));

        countQueryBlock.setFrom(new SQLSubqueryTableSource(select.clone(), "XX"));
        countQueryBlock.setWhere(new SQLBinaryOpExpr(new SQLIdentifierExpr("ROWNUM"), //
                                                     com.np.database.sql.ast.expr.SQLBinaryOperator.LessThanOrEqual, //
                                                     new SQLNumberExpr(count + offset), //
                                                     JdbcConstants.ORACLE));

        select.setOrderBy(null);
        if (offset <= 0) {
            select.setQuery(countQueryBlock);
            return true;
        }

        OracleSelectQueryBlock offsetQueryBlock = new OracleSelectQueryBlock();
        offsetQueryBlock.getSelectList().add(new SQLSelectItem(new SQLAllColumnExpr()));
        offsetQueryBlock.setFrom(new SQLSubqueryTableSource(new SQLSelect(countQueryBlock), "XXX"));
        offsetQueryBlock.setWhere(new SQLBinaryOpExpr(new SQLIdentifierExpr("RN"), //
                                                      com.np.database.sql.ast.expr.SQLBinaryOperator.GreaterThan, //
                                                      new SQLNumberExpr(offset), //
                                                      JdbcConstants.ORACLE));

        select.setQuery(offsetQueryBlock);
        return true;
    }

    private static boolean limitMySqlQueryBlock(SQLSelectQueryBlock queryBlock, String dbType, int offset, int count, boolean check) {
        SQLLimit limit = queryBlock.getLimit();
        if (limit != null) {
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            } else if (offset == 0) {
                if (limit.getOffset() != null) {
                    limit.setOffset(new SQLIntegerExpr(offset));
                }
            }

            if (check && limit.getRowCount() instanceof SQLNumericLiteralExpr) {
                int rowCount = ((SQLNumericLiteralExpr) limit.getRowCount()).getNumber().intValue();
                if (rowCount <= count && offset <= 0) {
                    return false;
                }
            } else if (check && limit.getRowCount() instanceof SQLVariantRefExpr) {
                return false;
            }

            limit.setRowCount(new SQLIntegerExpr(count));
        }

        if (limit == null) {
            limit = new SQLLimit();
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            }
            limit.setRowCount(new SQLIntegerExpr(count));
            queryBlock.setLimit(limit);
        }

        return true;
    }

    private static boolean limitUnion(SQLUnionQuery queryBlock, String dbType, int offset, int count, boolean check) {
        SQLLimit limit = queryBlock.getLimit();
        if (limit != null) {
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            }

            if (check && limit.getRowCount() instanceof SQLNumericLiteralExpr) {
                int rowCount = ((SQLNumericLiteralExpr) limit.getRowCount()).getNumber().intValue();
                if (rowCount <= count && offset <= 0) {
                    return false;
                }
            } else if (check && limit.getRowCount() instanceof SQLVariantRefExpr) {
                return false;
            }

            limit.setRowCount(new SQLIntegerExpr(count));
        }

        if (limit == null) {
            limit = new SQLLimit();
            if (offset > 0) {
                limit.setOffset(new SQLIntegerExpr(offset));
            }
            limit.setRowCount(new SQLIntegerExpr(count));
            queryBlock.setLimit(limit);
        }

        return true;
    }

    private static String count(SQLSelect select, String dbType) {
        if (select.getOrderBy() != null) {
            select.setOrderBy(null);
        }

        SQLSelectQuery query = select.getQuery();
        clearOrderBy(query);

        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectItem countItem = createCountItem(dbType);

            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) query;
            List<SQLSelectItem> selectList = queryBlock.getSelectList();

            if (queryBlock.getGroupBy() != null
                    && queryBlock.getGroupBy().getItems().size() > 0) {
                return createCountUseSubQuery(select, dbType);
            }
            
            int option = queryBlock.getDistionOption();
            if (option == com.np.database.sql.ast.SQLSetQuantifier.DISTINCT
                    && selectList.size() >= 1) {
                SQLAggregateExpr countExpr = new SQLAggregateExpr("COUNT", com.np.database.sql.ast.expr.SQLAggregateOption.DISTINCT);
                for (int i = 0; i < selectList.size(); ++i) {
                    countExpr.addArgument(selectList.get(i).getExpr());
                }
                selectList.clear();
                queryBlock.setDistionOption(0);
                queryBlock.addSelectItem(countExpr);
            } else {
                selectList.clear();
                selectList.add(countItem);
            }
            return NpSqlHelper.toSQLString(select, dbType);
        } else if (query instanceof SQLUnionQuery) {
            return createCountUseSubQuery(select, dbType);
        }

        throw new IllegalStateException();
    }

    private static String createCountUseSubQuery(SQLSelect select, String dbType) {
        SQLSelectQueryBlock countSelectQuery = createQueryBlock(dbType);

        SQLSelectItem countItem = createCountItem(dbType);
        countSelectQuery.getSelectList().add(countItem);

        SQLSubqueryTableSource fromSubquery = new SQLSubqueryTableSource(select);
        fromSubquery.setAlias("ALIAS_COUNT");
        countSelectQuery.setFrom(fromSubquery);

        SQLSelect countSelect = new SQLSelect(countSelectQuery);
        SQLSelectStatement countStmt = new SQLSelectStatement(countSelect, dbType);

        return NpSqlHelper.toSQLString(countStmt, dbType);
    }

    private static SQLSelectQueryBlock createQueryBlock(String dbType) {
        if (JdbcConstants.MYSQL.equals(dbType)
                || JdbcConstants.MARIADB.equals(dbType)
                || JdbcConstants.ALIYUN_ADS.equals(dbType)) {
            return new MySqlSelectQueryBlock();
        }

        if (JdbcConstants.MARIADB.equals(dbType)) {
            return new MySqlSelectQueryBlock();
        }

        if (JdbcConstants.H2.equals(dbType)) {
            return new MySqlSelectQueryBlock();
        }

        if (JdbcConstants.ORACLE.equals(dbType)) {
            return new OracleSelectQueryBlock();
        }

        if (JdbcConstants.POSTGRESQL.equals(dbType)) {
            return new PGSelectQueryBlock();
        }

        return new SQLSelectQueryBlock();
    }

    private static SQLSelectItem createCountItem(String dbType) {
        SQLAggregateExpr countExpr = new SQLAggregateExpr("COUNT");

        countExpr.addArgument(new SQLAllColumnExpr());

        SQLSelectItem countItem = new SQLSelectItem(countExpr);
        return countItem;
    }

    private static void clearOrderBy(SQLSelectQuery query) {
        if (query instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) query;
            if (queryBlock.getOrderBy() != null) {
                queryBlock.setOrderBy(null);
            }
            return;
        }

        if (query instanceof SQLUnionQuery) {
            SQLUnionQuery union = (SQLUnionQuery) query;
            if (union.getOrderBy() != null) {
                union.setOrderBy(null);
            }
            clearOrderBy(union.getLeft());
            clearOrderBy(union.getRight());
        }
    }
    
    /**
     * 
     * @param sql
     * @param dbType
     * @return if not exists limit, return -1;
     */
    public static int getLimit(String sql, String dbType) {
        List<SQLStatement> stmtList = NpSqlHelper.parseStatements(sql, dbType);

        if (stmtList.size() != 1) {
            return -1;
        }

        SQLStatement stmt = stmtList.get(0);

        if (stmt instanceof SQLSelectStatement) {
            SQLSelectStatement selectStmt = (SQLSelectStatement) stmt;
            SQLSelectQuery query = selectStmt.getSelect().getQuery();
            if (query instanceof SQLSelectQueryBlock) {
                if (query instanceof MySqlSelectQueryBlock) {
                    SQLLimit limit = ((MySqlSelectQueryBlock) query).getLimit();

                    if (limit == null) {
                        return -1;
                    }

                    SQLExpr rowCountExpr = limit.getRowCount();

                    if (rowCountExpr instanceof SQLNumericLiteralExpr) {
                        int rowCount = ((SQLNumericLiteralExpr) rowCountExpr).getNumber().intValue();
                        return rowCount;
                    }

                    return Integer.MAX_VALUE;
                }

                return -1;
            }
        }
        
        return -1;
    }

    public static boolean hasUnorderedLimit(String sql, String dbType) {
        List<SQLStatement> stmtList = NpSqlHelper.parseStatements(sql, dbType);

        if (JdbcConstants.MYSQL.equals(dbType)) {

            MySqlUnorderedLimitDetectVisitor visitor = new MySqlUnorderedLimitDetectVisitor();

            for (SQLStatement stmt : stmtList) {
                stmt.accept(visitor);
            }

            return visitor.unorderedLimitCount > 0;
        }

        if (JdbcConstants.ORACLE.equals(dbType)) {

            OracleUnorderedLimitDetectVisitor visitor = new OracleUnorderedLimitDetectVisitor();

            for (SQLStatement stmt : stmtList) {
                stmt.accept(visitor);
            }

            return visitor.unorderedLimitCount > 0;
        }

        throw new NpDbException("not supported. dbType : " + dbType);
    }

    private static class MySqlUnorderedLimitDetectVisitor extends MySqlASTVisitorAdapter {
        public int unorderedLimitCount;

        @Override
        public boolean visit(MySqlSelectQueryBlock x) {
            SQLOrderBy orderBy = x.getOrderBy();
            SQLLimit limit = x.getLimit();

            if (limit != null && (orderBy == null || orderBy.getItems().size() == 0)) {
                boolean subQueryHasOrderBy = false;
                SQLTableSource from = x.getFrom();
                if (from instanceof SQLSubqueryTableSource) {
                    SQLSubqueryTableSource subqueryTabSrc = (SQLSubqueryTableSource) from;
                    SQLSelect select = subqueryTabSrc.getSelect();
                    if (select.getQuery() instanceof SQLSelectQueryBlock) {
                        SQLSelectQueryBlock subquery = (SQLSelectQueryBlock) select.getQuery();
                        if (subquery.getOrderBy() != null && subquery.getOrderBy().getItems().size() > 0) {
                            subQueryHasOrderBy = true;
                        }
                    }
                }

                if (!subQueryHasOrderBy) {
                    unorderedLimitCount++;
                }
            }
            return true;
        }
    }

    private static class OracleUnorderedLimitDetectVisitor extends OracleASTVisitorAdapter {
        public int unorderedLimitCount;

        public boolean visit(SQLBinaryOpExpr x) {
            SQLExpr left = x.getLeft();
            SQLExpr right = x.getRight();

            boolean rownum = false;
            if (left instanceof SQLIdentifierExpr
                    && ((SQLIdentifierExpr) left).getName().equalsIgnoreCase("ROWNUM")
                    && right instanceof SQLLiteralExpr) {
                rownum = true;
            } else if (right instanceof SQLIdentifierExpr
                    && ((SQLIdentifierExpr) right).getName().equalsIgnoreCase("ROWNUM")
                    && left instanceof SQLLiteralExpr) {
                rownum = true;
            }

            OracleSelectQueryBlock selectQuery = null;
            if (rownum) {
                for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
                    if (parent instanceof SQLSelectQuery) {
                        if (parent instanceof OracleSelectQueryBlock) {
                            OracleSelectQueryBlock queryBlock = (OracleSelectQueryBlock) parent;
                            SQLTableSource from = queryBlock.getFrom();
                            if (from instanceof SQLExprTableSource) {
                                selectQuery = queryBlock;
                            } else if (from instanceof SQLSubqueryTableSource) {
                                SQLSelect subSelect = ((SQLSubqueryTableSource) from).getSelect();
                                if (subSelect.getQuery() instanceof OracleSelectQueryBlock) {
                                    selectQuery = (OracleSelectQueryBlock) subSelect.getQuery();
                                }
                            }
                        }
                        break;
                    }
                }
            }


            if (selectQuery != null) {
                SQLOrderBy orderBy = selectQuery.getOrderBy();

                SQLObject parent = selectQuery.getParent();
                if (orderBy == null && parent instanceof SQLSelect) {
                    SQLSelect select = (SQLSelect) parent;
                    orderBy = select.getOrderBy();
                }

                if (orderBy == null || orderBy.getItems().size() == 0) {
                    unorderedLimitCount++;
                }
            }

            return true;
        }

        @Override
        public boolean visit(OracleSelectQueryBlock queryBlock) {
            boolean isExprTableSrc = queryBlock.getFrom() instanceof SQLExprTableSource;

            if (!isExprTableSrc) {
                return true;
            }

            boolean rownum = false;
            for (SQLSelectItem item : queryBlock.getSelectList()) {
                SQLExpr itemExpr = item.getExpr();
                if (itemExpr instanceof SQLIdentifierExpr) {
                    if (((SQLIdentifierExpr) itemExpr).getName().equalsIgnoreCase("ROWNUM")) {
                        rownum = true;
                        break;
                    }
                }
            }

            if (!rownum) {
                return true;
            }

            SQLObject parent = queryBlock.getParent();
            if (!(parent instanceof SQLSelect)) {
                return true;
            }

            SQLSelect select = (SQLSelect) parent;

            if (select.getOrderBy() == null || select.getOrderBy().getItems().size() == 0) {
                unorderedLimitCount++;
            }

            return false;
        }
    }
}
