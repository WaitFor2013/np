
package com.np.database.sql.dialect.mysql.visitor.transform;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.expr.SQLBinaryOpExpr;
import com.np.database.sql.ast.expr.SQLExistsExpr;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLInSubQueryExpr;
import com.np.database.sql.ast.expr.SQLPropertyExpr;
import com.np.database.sql.ast.statement.SQLExprTableSource;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.ast.statement.SQLSubqueryTableSource;
import com.np.database.sql.ast.statement.SQLTableSource;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.np.database.sql.util.FnvHash;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.expr.*;
import com.np.database.sql.ast.statement.*;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;
import com.np.database.sql.util.FnvHash;

/**
 * Created by wenshao on 26/07/2017.
 */
public class NameResolveVisitor extends OracleASTVisitorAdapter {
    public boolean visit(SQLIdentifierExpr x) {
        SQLObject parent = x.getParent();

        if (parent instanceof SQLBinaryOpExpr
                && x.getResolvedColumn() == null) {
            SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr) parent;
            boolean isJoinCondition = binaryOpExpr.getLeft() instanceof SQLName
                    && binaryOpExpr.getRight() instanceof SQLName;
            if (isJoinCondition) {
                return false;
            }
        }

        String name = x.getName();

        if ("ROWNUM".equalsIgnoreCase(name)) {
            return false;
        }

        long hash = x.nameHashCode64();
        SQLTableSource tableSource = null;

        if (hash == FnvHash.Constants.LEVEL
                || hash == FnvHash.Constants.CONNECT_BY_ISCYCLE
                || hash == FnvHash.Constants.SYSTIMESTAMP) {
            return false;
        }

        if (parent instanceof SQLPropertyExpr) {
            return false;
        }

        for (; parent != null; parent = parent.getParent()) {
            if (parent instanceof SQLTableSource) {
                return false;
            }

            if (parent instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) parent;

                if (queryBlock.getInto() != null) {
                    return false;
                }

                if (queryBlock.getParent() instanceof SQLSelect) {
                    SQLObject pp = queryBlock.getParent().getParent();
                    if (pp instanceof SQLInSubQueryExpr || pp instanceof SQLExistsExpr) {
                        return false;
                    }
                }

                SQLTableSource from = queryBlock.getFrom();
                if (from instanceof SQLExprTableSource || from instanceof SQLSubqueryTableSource) {
                    String alias = from.getAlias();
                    if (alias != null) {
                        NpSqlHelper.replaceInParent(x, new SQLPropertyExpr(alias, name));
                    }
                }
                return false;
            }
        }
        return true;
    }

    public boolean visit(SQLPropertyExpr x) {
        String ownerName = x.getOwnernName();
        if (ownerName == null) {
            return super.visit(x);
        }

        for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock) parent;
                SQLTableSource tableSource = queryBlock.findTableSource(ownerName);
                if (tableSource == null) {
                    continue;
                }

                String alias = tableSource.computeAlias();
                if (tableSource != null
                        && ownerName.equalsIgnoreCase(alias)
                        && !ownerName.equals(alias)) {
                    x.setOwner(alias);
                }

                break;
            }
        }

        return super.visit(x);
    }
}
