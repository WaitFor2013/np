
package com.np.database.sql.dialect.h2.parser;

import com.np.database.sql.parser.SQLExprParser;
import com.np.database.sql.parser.SQLSelectListCache;
import com.np.database.sql.parser.SQLSelectParser;

public class H2SelectParser extends SQLSelectParser {

    public H2SelectParser(SQLExprParser exprParser){
        super(exprParser);
    }

    public H2SelectParser(SQLExprParser exprParser, SQLSelectListCache selectListCache){
        super(exprParser, selectListCache);
    }

    public H2SelectParser(String sql){
        this(new H2ExprParser(sql));
    }

    protected SQLExprParser createExprParser() {
        return new H2ExprParser(lexer);
    }
}
