
package com.np.database.sql.dialect.oracle.parser;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLTableSource;
import com.np.database.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.np.database.sql.parser.Lexer;
import com.np.database.sql.parser.ParserException;
import com.np.database.sql.parser.SQLStatementParser;
import com.np.database.sql.parser.Token;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLTableSource;
import com.np.database.sql.parser.Lexer;
import com.np.database.sql.parser.ParserException;
import com.np.database.sql.parser.SQLStatementParser;
import com.np.database.sql.parser.Token;

public class OracleUpdateParser extends SQLStatementParser {

    public OracleUpdateParser(String sql) {
        super(new OracleExprParser(sql));
    }

    public OracleUpdateParser(Lexer lexer){
        super(new OracleExprParser(lexer));
    }

    public OracleUpdateStatement parseUpdateStatement() {
        OracleUpdateStatement update = new OracleUpdateStatement();
        
        if (lexer.token() == Token.UPDATE) {
            lexer.nextToken();

            parseHints(update);

            if (lexer.identifierEquals("ONLY")) {
                update.setOnly(true);
            }

            SQLTableSource tableSource = this.exprParser.createSelectParser().parseTableSource();
            update.setTableSource(tableSource);

            if ((update.getAlias() == null) || (update.getAlias().length() == 0)) {
                update.setAlias(tableAlias());
            }
        }

        parseUpdateSet(update);

        parseWhere(update);

        parseReturn(update);

        parseErrorLoging(update);

        return update;
    }

    private void parseErrorLoging(OracleUpdateStatement update) {
        if (lexer.identifierEquals("LOG")) {
            throw new ParserException("TODO. " + lexer.info());
        }
    }

    private void parseReturn(OracleUpdateStatement update) {
        if (lexer.identifierEquals("RETURN") || lexer.token() == Token.RETURNING) {
            lexer.nextToken();

            for (;;) {
                SQLExpr item = this.exprParser.expr();
                update.getReturning().add(item);

                if (lexer.token() == Token.COMMA) {
                    lexer.nextToken();
                    continue;
                }

                break;
            }

            accept(Token.INTO);

            for (;;) {
                SQLExpr item = this.exprParser.expr();
                update.getReturningInto().add(item);

                if (lexer.token() == Token.COMMA) {
                    lexer.nextToken();
                    continue;
                }

                break;
            }
        }
    }

    private void parseHints(OracleUpdateStatement update) {
        this.exprParser.parseHints(update.getHints());
    }

    private void parseWhere(OracleUpdateStatement update) {
        if (lexer.token() == (Token.WHERE)) {
            lexer.nextToken();
            update.setWhere(this.exprParser.expr());
        }
    }

}
