
package com.np.database.sql.visitor;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.ast.statement.SQLSelectStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.np.database.sql.dialect.oracle.visitor.OracleParameterizedOutputVisitor;
import com.np.database.sql.dialect.postgresql.visitor.PGOutputVisitor;
import com.np.database.sql.parser.SQLParserFeature;
import com.np.database.sql.parser.SQLParserUtils;
import com.np.database.sql.parser.SQLSelectListCache;
import com.np.database.sql.parser.SQLStatementParser;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.util.JdbcUtils;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.ast.statement.SQLSelectStatement;
import com.np.database.sql.parser.SQLParserFeature;
import com.np.database.sql.parser.SQLParserUtils;
import com.np.database.sql.parser.SQLSelectListCache;
import com.np.database.sql.parser.SQLStatementParser;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.util.JdbcUtils;

import java.util.List;

public class ParameterizedOutputVisitorUtils {
    private final static SQLParserFeature[] defaultFeatures = {
            SQLParserFeature.EnableSQLBinaryOpExprGroup,
            SQLParserFeature.UseInsertColumnsCache,
            SQLParserFeature.OptimizedForParameterized
    };

    private final static SQLParserFeature[] defaultFeatures2 = {
            SQLParserFeature.EnableSQLBinaryOpExprGroup,
            SQLParserFeature.UseInsertColumnsCache,
            SQLParserFeature.OptimizedForParameterized,
            SQLParserFeature.OptimizedForForParameterizedSkipValue,
    };

    public static String parameterize(String sql, String dbType) {
        return parameterize(sql, dbType, null, null);
    }

    public static String parameterize(String sql
            , String dbType
            , SQLSelectListCache selectListCache) {
        return parameterize(sql, dbType, selectListCache, null);
    }

    public static String parameterize(String sql
            , String dbType
            , List<Object> outParameters) {
        return parameterize(sql, dbType, null, outParameters);
    }


    private static void configVisitorFeatures(ParameterizedVisitor visitor, VisitorFeature ...features) {
        if(features != null) {
            for (int i = 0; i < features.length; i++) {
                visitor.config(features[i], true);
            }
        }
    }

    public static String parameterize(String sql
            , String dbType
            , List<Object> outParameters, VisitorFeature ...features) {
        return parameterize(sql, dbType, null, outParameters, features);
    }

    public static String parameterize(String sql
            , String dbType
            , SQLSelectListCache selectListCache, List<Object> outParameters, VisitorFeature ...visitorFeatures) {

        final SQLParserFeature[] features = outParameters == null
                ? defaultFeatures2
                : defaultFeatures;

        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, features);

        if (selectListCache != null) {
            parser.setSelectListCache(selectListCache);
        }

        List<SQLStatement> statementList = parser.parseStatementList();
        if (statementList.size() == 0) {
            return sql;
        }

        StringBuilder out = new StringBuilder(sql.length());
        ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);
        if (outParameters != null) {
            visitor.setOutputParameters(outParameters);
        }
        configVisitorFeatures(visitor, visitorFeatures);

        for (int i = 0; i < statementList.size(); i++) {
            SQLStatement stmt = statementList.get(i);

            if (i > 0) {
                SQLStatement preStmt = statementList.get(i - 1);

                if (preStmt.getClass() == stmt.getClass()) {
                    StringBuilder buf = new StringBuilder();
                    ParameterizedVisitor v1 = createParameterizedOutputVisitor(buf, dbType);
                    preStmt.accept(v1);
                    if (out.toString().equals(buf.toString())) {
                        continue;
                    }
                }

                if (!preStmt.isAfterSemi()) {
                    out.append(";\n");
                } else {
                    out.append('\n');
                }
            }

            if (stmt.hasBeforeComment()) {
                stmt.getBeforeCommentsDirect().clear();
            }

            Class<?> stmtClass = stmt.getClass();
            if (stmtClass == SQLSelectStatement.class) { // only for performance
                SQLSelectStatement selectStatement = (SQLSelectStatement) stmt;
                visitor.visit(selectStatement);
                visitor.postVisit(selectStatement);
            } else {
                stmt.accept(visitor);
            }
        }

        if (visitor.getReplaceCount() == 0
                && parser.getLexer().getCommentCount() == 0 && sql.charAt(0) != '/') {
            return sql;
        }

        return out.toString();
    }

    public static long parameterizeHash(String sql
            , String dbType
            , List<Object> outParameters) {
        return parameterizeHash(sql, dbType, null, outParameters, null);
    }

    public static long parameterizeHash(String sql
            , String dbType
            , SQLSelectListCache selectListCache
            , List<Object> outParameters, VisitorFeature ...visitorFeatures) {

        final SQLParserFeature[] features = outParameters == null
                ? defaultFeatures2
                : defaultFeatures;

        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType, features);

        if (selectListCache != null) {
            parser.setSelectListCache(selectListCache);
        }

        List<SQLStatement> statementList = parser.parseStatementList();
        final int stmtSize = statementList.size();
        if (stmtSize == 0) {
            return 0L;
        }

        StringBuilder out = new StringBuilder(sql.length());
        ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);
        if (outParameters != null) {
            visitor.setOutputParameters(outParameters);
        }
        configVisitorFeatures(visitor, visitorFeatures);

        if (stmtSize == 1) {
            SQLStatement stmt = statementList.get(0);
            if (stmt.getClass() == SQLSelectStatement.class) {
                SQLSelectStatement selectStmt = (SQLSelectStatement) stmt;

                if (selectListCache != null) {
                    SQLSelectQueryBlock queryBlock = selectStmt.getSelect().getQueryBlock();
                    if (queryBlock != null) {
                        String cachedSelectList = queryBlock.getCachedSelectList();
                        long cachedSelectListHash = queryBlock.getCachedSelectListHash();
                        if (cachedSelectList != null) {
                            visitor.config(VisitorFeature.OutputSkipSelectListCacheString, true);
                        }

                        visitor.visit(selectStmt);
                        return FnvHash.fnv1a_64_lower(cachedSelectListHash, out);
                    }
                }

                visitor.visit(selectStmt);
            } else if (stmt.getClass() == MySqlInsertStatement.class) {
                MySqlInsertStatement insertStmt = (MySqlInsertStatement) stmt;
                String columnsString = insertStmt.getColumnsString();
                if (columnsString != null) {
                    long columnsStringHash = insertStmt.getColumnsStringHash();
                    visitor.config(VisitorFeature.OutputSkipInsertColumnsString, true);

                    ((MySqlASTVisitor) visitor).visit(insertStmt);
                    return FnvHash.fnv1a_64_lower(columnsStringHash, out);
                }
            } else {
                stmt.accept(visitor);
            }

            return FnvHash.fnv1a_64_lower(out);
        }

        for (int i = 0; i < statementList.size(); i++) {
            if (i > 0) {
                out.append(";\n");
            }
            SQLStatement stmt = statementList.get(i);

            if (stmt.hasBeforeComment()) {
                stmt.getBeforeCommentsDirect().clear();
            }

            Class<?> stmtClass = stmt.getClass();
            if (stmtClass == SQLSelectStatement.class) { // only for performance
                SQLSelectStatement selectStatement = (SQLSelectStatement) stmt;
                visitor.visit(selectStatement);
                visitor.postVisit(selectStatement);
            } else {
                stmt.accept(visitor);
            }
        }

        return FnvHash.fnv1a_64_lower(out);
    }

    public static String parameterize(List<SQLStatement> statementList, String dbType) {
        StringBuilder out = new StringBuilder();
        ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);

        for (int i = 0; i < statementList.size(); i++) {
            if (i > 0) {
                out.append(";\n");
            }
            SQLStatement stmt = statementList.get(i);

            if (stmt.hasBeforeComment()) {
                stmt.getBeforeCommentsDirect().clear();
            }
            stmt.accept(visitor);
        }

        return out.toString();
    }

    public static ParameterizedVisitor createParameterizedOutputVisitor(Appendable out, String dbType) {
        if (JdbcUtils.ORACLE.equals(dbType) || JdbcUtils.ALI_ORACLE.equals(dbType)) {
            return new OracleParameterizedOutputVisitor(out);
        }

        if (JdbcUtils.MYSQL.equals(dbType)
            || JdbcUtils.MARIADB.equals(dbType)
            || JdbcUtils.H2.equals(dbType)) {
            return new MySqlOutputVisitor(out, true);
        }

        if (JdbcUtils.POSTGRESQL.equals(dbType)
                || JdbcUtils.ENTERPRISEDB.equals(dbType)) {
            return new PGOutputVisitor(out, true);
        }

        if (JdbcUtils.ELASTIC_SEARCH.equals(dbType)) {
            return new MySqlOutputVisitor(out, true);
        }

        return new SQLASTOutputVisitor(out, true);
    }

    public static String restore(String sql, String dbType, List<Object> parameters) {
        List<SQLStatement> stmtList = NpSqlHelper.parseStatements(sql, dbType);

        StringBuilder out = new StringBuilder();
        SQLASTOutputVisitor visitor = NpSqlHelper.createOutputVisitor(out, dbType);
        visitor.setInputParameters(parameters);

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }

        return out.toString();
    }
}
