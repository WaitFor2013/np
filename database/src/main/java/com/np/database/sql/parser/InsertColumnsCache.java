
package com.np.database.sql.parser;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.util.FnvHash;

public class InsertColumnsCache {
    public static InsertColumnsCache global;

    static {
        global = new InsertColumnsCache(8192);
    }

    public ConcurrentMap<Long, Entry> cache = new ConcurrentHashMap<Long, Entry>();

    private final Entry[]   buckets;
    private final int       indexMask;

    public InsertColumnsCache(int tableSize){
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
    }

    public final Entry get(long hashCode64) {
        final int bucket = ((int) hashCode64) & indexMask;
        for (Entry entry = buckets[bucket]; entry != null; entry = entry.next) {
            if (hashCode64 == entry.hashCode64) {
                return entry;
            }
        }

        return null;
    }

    public boolean put(long hashCode64, String columnsString, String columnsFormattedString, List<SQLExpr> columns) {
        final int bucket = ((int) hashCode64) & indexMask;

        for (Entry entry = buckets[bucket]; entry != null; entry = entry.next) {
            if (hashCode64 == entry.hashCode64) {
                return true;
            }
        }

        Entry entry = new Entry(hashCode64, columnsString, columnsFormattedString, columns, buckets[bucket]);
        buckets[bucket] = entry;  // 并发是处理时会可能导致缓存丢失，但不影响正确性

        return false;
    }

    public final static class Entry {
        public final long hashCode64;
        public final String columnsString;
        public final String columnsFormattedString;
        public final long columnsFormattedStringHash;
        public final List<SQLExpr> columns;
        public final Entry next;

        public Entry(long hashCode64, String columnsString, String columnsFormattedString, List<SQLExpr> columns, Entry next) {
            this.hashCode64 = hashCode64;
            this.columnsString = columnsString;
            this.columnsFormattedString = columnsFormattedString;
            this.columnsFormattedStringHash = FnvHash.fnv1a_64_lower(columnsFormattedString);
            this.columns = columns;
            this.next = next;
        }
    }
}
