
package com.np.database.sql.dialect.mysql.parser;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLSetQuantifier;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLLiteralExpr;
import com.np.database.sql.ast.expr.SQLVariantRefExpr;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.ast.statement.SQLSelectQuery;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.ast.statement.SQLTableSource;
import com.np.database.sql.ast.statement.SQLUnionQuery;
import com.np.database.sql.dialect.mysql.ast.MySqlForceIndexHint;
import com.np.database.sql.dialect.mysql.ast.MySqlIgnoreIndexHint;
import com.np.database.sql.dialect.mysql.ast.MySqlIndexHint;
import com.np.database.sql.dialect.mysql.ast.MySqlIndexHintImpl;
import com.np.database.sql.dialect.mysql.ast.MySqlUseIndexHint;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.np.database.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.np.database.sql.parser.ParserException;
import com.np.database.sql.parser.SQLExprParser;
import com.np.database.sql.parser.SQLSelectParser;
import com.np.database.sql.parser.Token;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLSetQuantifier;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLLiteralExpr;
import com.np.database.sql.ast.expr.SQLVariantRefExpr;
import com.np.database.sql.ast.statement.*;
import com.np.database.sql.dialect.mysql.ast.*;
import com.np.database.sql.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import com.np.database.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.np.database.sql.parser.ParserException;
import com.np.database.sql.parser.SQLExprParser;
import com.np.database.sql.parser.SQLSelectParser;
import com.np.database.sql.parser.Token;

/**
 * 
 * @author zz [455910092@qq.com]
 */
public class MySqlSelectIntoParser extends SQLSelectParser {
	private List<SQLExpr> argsList;

    public MySqlSelectIntoParser(SQLExprParser exprParser){
        super(exprParser);
    }

    public MySqlSelectIntoParser(String sql){
        this(new MySqlExprParser(sql));
    }
    
    public MySqlSelectIntoStatement parseSelectInto()
    {
    	SQLSelect select=select();
    	MySqlSelectIntoStatement stmt=new MySqlSelectIntoStatement();
    	stmt.setSelect(select);
    	stmt.setVarList(argsList);
    	return stmt;
    	
    }

    @Override
    public SQLSelectQuery query() {
        if (lexer.token() == (Token.LPAREN)) {
            lexer.nextToken();

            SQLSelectQuery select = query();
            accept(Token.RPAREN);

            return queryRest(select);
        }

        MySqlSelectQueryBlock queryBlock = new MySqlSelectQueryBlock();

        if (lexer.token() == Token.SELECT) {
            lexer.nextToken();

            if (lexer.token() == Token.HINT) {
                this.exprParser.parseHints(queryBlock.getHints());
            }

            if (lexer.token() == Token.COMMENT) {
                lexer.nextToken();
            }

            if (lexer.token() == (Token.DISTINCT)) {
                queryBlock.setDistionOption(SQLSetQuantifier.DISTINCT);
                lexer.nextToken();
            } else if (lexer.identifierEquals("DISTINCTROW")) {
                queryBlock.setDistionOption(SQLSetQuantifier.DISTINCTROW);
                lexer.nextToken();
            } else if (lexer.token() == (Token.ALL)) {
                queryBlock.setDistionOption(SQLSetQuantifier.ALL);
                lexer.nextToken();
            }

            if (lexer.identifierEquals("HIGH_PRIORITY")) {
                queryBlock.setHignPriority(true);
                lexer.nextToken();
            }

            if (lexer.identifierEquals("STRAIGHT_JOIN")) {
                queryBlock.setStraightJoin(true);
                lexer.nextToken();
            }

            if (lexer.identifierEquals("SQL_SMALL_RESULT")) {
                queryBlock.setSmallResult(true);
                lexer.nextToken();
            }

            if (lexer.identifierEquals("SQL_BIG_RESULT")) {
                queryBlock.setBigResult(true);
                lexer.nextToken();
            }

            if (lexer.identifierEquals("SQL_BUFFER_RESULT")) {
                queryBlock.setBufferResult(true);
                lexer.nextToken();
            }

            if (lexer.identifierEquals("SQL_CACHE")) {
                queryBlock.setCache(true);
                lexer.nextToken();
            }

            if (lexer.identifierEquals("SQL_NO_CACHE")) {
                queryBlock.setCache(false);
                lexer.nextToken();
            }

            if (lexer.identifierEquals("SQL_CALC_FOUND_ROWS")) {
                queryBlock.setCalcFoundRows(true);
                lexer.nextToken();
            }

            parseSelectList(queryBlock);
            
            argsList=parseIntoArgs();
        }

        parseFrom(queryBlock);

        parseWhere(queryBlock);

        parseGroupBy(queryBlock);

        queryBlock.setOrderBy(this.exprParser.parseOrderBy());

        if (lexer.token() == Token.LIMIT) {
            queryBlock.setLimit(this.exprParser.parseLimit());
        }

        if (lexer.token() == Token.PROCEDURE) {
            lexer.nextToken();
            throw new ParserException("TODO. " + lexer.info());
        }

        parseInto(queryBlock);

        if (lexer.token() == Token.FOR) {
            lexer.nextToken();
            accept(Token.UPDATE);

            queryBlock.setForUpdate(true);
        }

        if (lexer.token() == Token.LOCK) {
            lexer.nextToken();
            accept(Token.IN);
            acceptIdentifier("SHARE");
            acceptIdentifier("MODE");
            queryBlock.setLockInShareMode(true);
        }

        return queryRest(queryBlock);
    }
    /**
     * parser the select into arguments
     * @return
     */
	protected List<SQLExpr> parseIntoArgs() {
		
		List<SQLExpr> args=new ArrayList<SQLExpr>();
		if (lexer.token() == (Token.INTO)) {
			accept(Token.INTO);
			//lexer.nextToken();
			for (;;) {
				SQLExpr var = exprParser.primary();
				if (var instanceof SQLIdentifierExpr) {
					var = new SQLVariantRefExpr(
							((SQLIdentifierExpr) var).getName());
				}
				args.add(var);
				if (lexer.token() == Token.COMMA) {
					accept(Token.COMMA);
					continue;
				}
				else
				{
					break;
				}
			}
		}
		return args;
	}
    
    
    protected void parseInto(SQLSelectQueryBlock queryBlock) {
        if (lexer.token() == (Token.INTO)) {
            lexer.nextToken();

            if (lexer.identifierEquals("OUTFILE")) {
                lexer.nextToken();

                MySqlOutFileExpr outFile = new MySqlOutFileExpr();
                outFile.setFile(expr());

                queryBlock.setInto(outFile);

                if (lexer.identifierEquals("FIELDS") || lexer.identifierEquals("COLUMNS")) {
                    lexer.nextToken();

                    if (lexer.identifierEquals("TERMINATED")) {
                        lexer.nextToken();
                        accept(Token.BY);
                    }
                    outFile.setColumnsTerminatedBy((SQLLiteralExpr) expr());

                    if (lexer.identifierEquals("OPTIONALLY")) {
                        lexer.nextToken();
                        outFile.setColumnsEnclosedOptionally(true);
                    }

                    if (lexer.identifierEquals("ENCLOSED")) {
                        lexer.nextToken();
                        accept(Token.BY);
                        outFile.setColumnsEnclosedBy((SQLLiteralExpr) expr());
                    }

                    if (lexer.identifierEquals("ESCAPED")) {
                        lexer.nextToken();
                        accept(Token.BY);
                        outFile.setColumnsEscaped((SQLLiteralExpr) expr());
                    }
                }

                if (lexer.identifierEquals("LINES")) {
                    lexer.nextToken();

                    if (lexer.identifierEquals("STARTING")) {
                        lexer.nextToken();
                        accept(Token.BY);
                        outFile.setLinesStartingBy((SQLLiteralExpr) expr());
                    } else {
                        lexer.identifierEquals("TERMINATED");
                        lexer.nextToken();
                        accept(Token.BY);
                        outFile.setLinesTerminatedBy((SQLLiteralExpr) expr());
                    }
                }
            } else {
                queryBlock.setInto(this.exprParser.name());
            }
        }
    }

    protected SQLTableSource parseTableSourceRest(SQLTableSource tableSource) {
        if (lexer.identifierEquals("USING")) {
            return tableSource;
        }

        parseIndexHintList(tableSource);
	
        return super.parseTableSourceRest(tableSource);
    }

    private void parseIndexHintList(SQLTableSource tableSource) {
	if (lexer.token() == Token.USE) {
            lexer.nextToken();
            MySqlUseIndexHint hint = new MySqlUseIndexHint();
            parseIndexHint(hint);
            tableSource.getHints().add(hint);
	    parseIndexHintList(tableSource);
        }

        if (lexer.identifierEquals("IGNORE")) {
            lexer.nextToken();
            MySqlIgnoreIndexHint hint = new MySqlIgnoreIndexHint();
            parseIndexHint(hint);
            tableSource.getHints().add(hint);
	    parseIndexHintList(tableSource);
        }

        if (lexer.identifierEquals("FORCE")) {
            lexer.nextToken();
            MySqlForceIndexHint hint = new MySqlForceIndexHint();
            parseIndexHint(hint);
            tableSource.getHints().add(hint);
	    parseIndexHintList(tableSource);
        }
    }

    private void parseIndexHint(MySqlIndexHintImpl hint) {
        if (lexer.token() == Token.INDEX) {
            lexer.nextToken();
        } else {
            accept(Token.KEY);
        }

        if (lexer.token() == Token.FOR) {
            lexer.nextToken();

            if (lexer.token() == Token.JOIN) {
                lexer.nextToken();
                hint.setOption(MySqlIndexHint.Option.JOIN);
            } else if (lexer.token() == Token.ORDER) {
                lexer.nextToken();
                accept(Token.BY);
                hint.setOption(MySqlIndexHint.Option.ORDER_BY);
            } else {
                accept(Token.GROUP);
                accept(Token.BY);
                hint.setOption(MySqlIndexHint.Option.GROUP_BY);
            }
        }

        accept(Token.LPAREN);
        if (lexer.token() == Token.PRIMARY) {
            lexer.nextToken();
            hint.getIndexList().add(new SQLIdentifierExpr("PRIMARY"));
        } else {
            this.exprParser.names(hint.getIndexList());
        }
        accept(Token.RPAREN);
    }

    public SQLUnionQuery unionRest(SQLUnionQuery union) {
        if (lexer.token() == Token.LIMIT) {
            union.setLimit(this.exprParser.parseLimit());
        }
        return super.unionRest(union);
    }
    
    public MySqlExprParser getExprParser() {
        return (MySqlExprParser) exprParser;
    }
}
