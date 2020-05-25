
package com.np.database.sql.dialect.h2.parser;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.expr.SQLQueryExpr;
import com.np.database.sql.ast.statement.SQLInsertInto;
import com.np.database.sql.ast.statement.SQLInsertStatement;
import com.np.database.sql.ast.statement.SQLReplaceStatement;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.parser.Lexer;
import com.np.database.sql.parser.SQLParserFeature;
import com.np.database.sql.parser.SQLStatementParser;
import com.np.database.sql.parser.Token;
import com.np.database.sql.util.JdbcConstants;

public class H2StatementParser extends SQLStatementParser {
    public H2StatementParser(String sql) {
        super (new H2ExprParser(sql));
    }

    public H2StatementParser(String sql, SQLParserFeature... features) {
        super (new H2ExprParser(sql, features));
    }

    public H2StatementParser(Lexer lexer){
        super(new H2ExprParser(lexer));
    }

    public H2SelectParser createSQLSelectParser() {
        return new H2SelectParser(this.exprParser, selectListCache);
    }

    @Override
    public SQLStatement parseMerge() {
        accept(Token.MERGE);
        accept(Token.INTO);

        SQLReplaceStatement stmt = new SQLReplaceStatement();
        stmt.setDbType(JdbcConstants.H2);

        SQLName tableName = exprParser.name();
        stmt.setTableName(tableName);

        if (lexer.token() == Token.KEY) {
            lexer.nextToken();
            accept(Token.LPAREN);
            this.exprParser.exprList(stmt.getColumns(), stmt);
            accept(Token.RPAREN);
        }

        if (lexer.token() == Token.VALUES || lexer.identifierEquals("VALUE")) {
            lexer.nextToken();

            parseValueClause(stmt.getValuesList(), 0, stmt);
        } else if (lexer.token() == Token.SELECT) {
            SQLQueryExpr queryExpr = (SQLQueryExpr) this.exprParser.expr();
            stmt.setQuery(queryExpr);
        } else if (lexer.token() == Token.LPAREN) {
            SQLSelect select = this.createSQLSelectParser().select();
            SQLQueryExpr queryExpr = new SQLQueryExpr(select);
            stmt.setQuery(queryExpr);
        }

        return stmt;
    }

    @Override
    protected void parseInsert0(SQLInsertInto insertStatement, boolean acceptSubQuery) {
        super.parseInsert0(insertStatement, acceptSubQuery);
        parseSetStatement(insertStatement);
    }

    private void parseSetStatement(SQLInsertInto insertStatement) {
        if (lexer.token() == Token.SET) {
            lexer.nextToken();
            SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
            insertStatement.addValueCause(values);

            for (; ; ) {
                SQLName name = this.exprParser.name();
                insertStatement.addColumn(name);
                if (lexer.token() == Token.EQ) {
                    lexer.nextToken();
                } else {
                    accept(Token.COLONEQ);
                }
                values.addValue(this.exprParser.expr());

                if (lexer.token() == Token.COMMA) {
                    lexer.nextToken();
                    continue;
                }
                break;
            }
        }
    }

}
