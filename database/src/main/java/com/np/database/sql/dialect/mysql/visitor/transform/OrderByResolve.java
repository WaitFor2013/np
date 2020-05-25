
package com.np.database.sql.dialect.mysql.visitor.transform;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLOrderBy;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.ast.statement.SQLSelectItem;
import com.np.database.sql.ast.statement.SQLSelectOrderByItem;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLOrderBy;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.ast.statement.SQLSelectItem;
import com.np.database.sql.ast.statement.SQLSelectOrderByItem;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.np.database.sql.util.FnvHash;

/**
 * Created by wenshao on 27/07/2017.
 */
public class OrderByResolve extends OracleASTVisitorAdapter {
    final static long DBMS_RANDOM_VALUE = FnvHash.hashCode64("DBMS_RANDOM.value");

    public boolean visit(SQLSelect x) {
        SQLSelectQueryBlock queryBlock = x.getQueryBlock();
        if (queryBlock == null) {
            return super.visit(x);
        }

        if (x.getOrderBy() != null && queryBlock.isForUpdate() && queryBlock.getOrderBy() == null) {
            queryBlock.setOrderBy(x.getOrderBy());
            x.setOrderBy(null);
        }

        SQLOrderBy orderBy = queryBlock.getOrderBy();
        if (orderBy == null) {
            return super.visit(x);
        }


        if (!queryBlock.selectItemHasAllColumn(false)) {
            List<SQLSelectOrderByItem> notContainsOrderBy = new ArrayList<SQLSelectOrderByItem>();

            for (SQLSelectOrderByItem orderByItem : orderBy.getItems()) {
                SQLExpr orderByExpr = orderByItem.getExpr();

                if (orderByExpr instanceof SQLName) {
                    if (((SQLName) orderByExpr).hashCode64() == DBMS_RANDOM_VALUE) {
                        continue;
                    }

                    long hashCode64 = ((SQLName) orderByExpr).nameHashCode64();
                    SQLSelectItem selectItem = queryBlock.findSelectItem(hashCode64);
                    if (selectItem == null) {
                        queryBlock.addSelectItem(orderByExpr.clone());
                    }
                }
            }

            if (notContainsOrderBy.size() > 0) {
                for (SQLSelectOrderByItem orderByItem : notContainsOrderBy) {
                    queryBlock.addSelectItem(orderByItem.getExpr());
                }

                OracleSelectQueryBlock queryBlock1 = new OracleSelectQueryBlock();
                queryBlock1.setFrom(queryBlock, "x");
                x.setQuery(queryBlock1);
            }
        }



        return super.visit(x);
    }
}
