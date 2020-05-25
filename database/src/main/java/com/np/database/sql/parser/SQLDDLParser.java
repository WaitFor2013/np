
package com.np.database.sql.parser;

import com.np.database.sql.ast.statement.SQLTableConstraint;

public class SQLDDLParser extends SQLStatementParser {

    public SQLDDLParser(String sql){
        super(sql);
    }

    public SQLDDLParser(SQLExprParser exprParser){
        super(exprParser);
    }

    protected SQLTableConstraint parseConstraint() {
        if (lexer.token == Token.CONSTRAINT) {
            lexer.nextToken();
        }

        if (lexer.token == Token.IDENTIFIER) {
            this.exprParser.name();
            throw new ParserException("TODO. " + lexer.info());
        }

        if (lexer.token == Token.PRIMARY) {
            lexer.nextToken();
            accept(Token.KEY);

            throw new ParserException("TODO. " + lexer.info());
        }

        throw new ParserException("TODO " + lexer.info());
    }
}
