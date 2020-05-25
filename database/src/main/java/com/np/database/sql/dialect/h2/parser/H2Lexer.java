
package com.np.database.sql.dialect.h2.parser;

import java.util.HashMap;
import java.util.Map;

import com.np.database.sql.parser.Keywords;
import com.np.database.sql.parser.Lexer;
import com.np.database.sql.parser.SQLParserFeature;
import com.np.database.sql.parser.Token;

public class H2Lexer extends Lexer {
    public final static Keywords DEFAULT_H2_KEYWORDS;

    static {
        Map<String, Token> map = new HashMap<String, Token>();

        map.putAll(Keywords.DEFAULT_KEYWORDS.getKeywords());

        map.put("OF", Token.OF);
        map.put("CONCAT", Token.CONCAT);
        map.put("CONTINUE", Token.CONTINUE);
        map.put("MERGE", Token.MERGE);
        map.put("USING", Token.USING);

        map.put("ROW", Token.ROW);
        map.put("LIMIT", Token.LIMIT);
        map.put("IF", Token.IF);

        DEFAULT_H2_KEYWORDS = new Keywords(map);
    }

    public H2Lexer(String input){
        super(input);
        super.keywods = DEFAULT_H2_KEYWORDS;
    }

    public H2Lexer(String input, SQLParserFeature... features){
        super(input);
        super.keywods = DEFAULT_H2_KEYWORDS;
        for (SQLParserFeature feature : features) {
            config(feature, true);
        }
    }
}
