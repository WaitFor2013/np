
package com.np.database.sql.parser;

import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.util.FnvHash;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class SQLSelectListCache {
    private final String                    dbType;
    private final List<Entry>               entries         = new CopyOnWriteArrayList<Entry>();

    public SQLSelectListCache(String dbType) {
        this.dbType = dbType;
    }

    public void add(String select) {
        if (select == null || select.length() == 0) {
            return;
        }

        SQLSelectParser selectParser = SQLParserUtils.createSQLStatementParser(select, dbType)
                                                     .createSQLSelectParser();
        SQLSelectQueryBlock queryBlock = SQLParserUtils.createSelectQueryBlock(dbType);
        selectParser.accept(Token.SELECT);

        selectParser.parseSelectList(queryBlock);

        selectParser.accept(Token.FROM);
        selectParser.accept(Token.EOF);

        String printSql = queryBlock.toString();
        long printSqlHash = FnvHash.fnv1a_64_lower(printSql);
        entries.add(
                new Entry(select.substring(6)
                        , queryBlock
                        , printSql
                        , printSqlHash)
        );

        if (entries.size() > 5) {
            log.warn("SelectListCache is too large.");
        }
    }

    public int getSize() {
        return entries.size();
    }

    public void clear() {
        entries.clear();
    }

    public boolean match(Lexer lexer, SQLSelectQueryBlock queryBlock) {
        if (lexer.token != Token.SELECT) {
            return false;
        }

        int pos = lexer.pos;
        String text = lexer.text;

        for (int i = 0; i < entries.size(); i++) {
            Entry entry = entries.get(i);
            String block = entry.sql;
            if (text.startsWith(block, pos)) {
                //SQLSelectQueryBlock queryBlockCached = queryBlockCache.get(i);
                // queryBlockCached.cloneSelectListTo(queryBlock);
                queryBlock.setCachedSelectList(entry.printSql, entry.printSqlHash);

                int len = pos + block.length();
                lexer.reset(len, text.charAt(len), Token.FROM);
                return true;
            }
        }
        return false;
    }

    private static class Entry {
        public final String              sql;
        public final SQLSelectQueryBlock queryBlock;
        public final String              printSql;
        public final long                printSqlHash;

        public Entry(String sql, SQLSelectQueryBlock queryBlock, String printSql, long printSqlHash) {
            this.sql = sql;
            this.queryBlock = queryBlock;
            this.printSql = printSql;
            this.printSqlHash = printSqlHash;
        }
    }
}
