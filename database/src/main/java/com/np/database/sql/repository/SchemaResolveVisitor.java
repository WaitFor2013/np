
package com.np.database.sql.repository;

import java.util.HashMap;
import java.util.Map;

import com.np.database.sql.ast.SQLDeclareItem;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.statement.SQLTableSource;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLDeclareItem;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.statement.SQLTableSource;

/**
 * Created by wenshao on 03/08/2017.
 */
public interface SchemaResolveVisitor extends SQLASTVisitor {

    boolean isEnabled(Option option);

    public static enum Option {
        ResolveAllColumn,
        ResolveIdentifierAlias
        ;
        private Option() {
            mask = (1 << ordinal());
        }

        public final int mask;

        public static int of(Option... options) {
            if (options == null) {
                return 0;
            }

            int value = 0;

            for (Option option : options) {
                value |= option.mask;
            }

            return value;
        }
    }

    SchemaRepository getRepository();

    Context getContext();
    Context createContext(SQLObject object);
    void popContext();

    static class Context {
        public final Context parent;
        public final SQLObject object;

        private SQLTableSource tableSource;

        private SQLTableSource from;

        private Map<Long, SQLTableSource> tableSourceMap;

        protected Map<Long, SQLDeclareItem> declares;

        public Context(SQLObject object, Context parent) {
            this.object = object;
            this.parent = parent;
        }

        public SQLTableSource getFrom() {
            return from;
        }

        public void setFrom(SQLTableSource from) {
            this.from = from;
        }

        public SQLTableSource getTableSource() {
            return tableSource;
        }

        public void setTableSource(SQLTableSource tableSource) {
            this.tableSource = tableSource;
        }

        public void addTableSource(long alias_hash, SQLTableSource tableSource) {
            tableSourceMap.put(alias_hash, tableSource);
        }

        protected void declare(SQLDeclareItem x) {
            if (declares == null) {
                declares = new HashMap<Long, SQLDeclareItem>();
            }
            declares.put(x.getName().nameHashCode64(), x);
        }

        protected SQLDeclareItem findDeclare(long nameHash) {
            if (declares == null) {
                return null;
            }
            return declares.get(nameHash);
        }
    }
}
