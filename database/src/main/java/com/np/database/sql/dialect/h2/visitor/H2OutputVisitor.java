
package com.np.database.sql.dialect.h2.visitor;

import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLQueryExpr;
import com.np.database.sql.ast.statement.SQLInsertStatement;
import com.np.database.sql.ast.statement.SQLReplaceStatement;
import com.np.database.sql.visitor.SQLASTOutputVisitor;

public class H2OutputVisitor extends SQLASTOutputVisitor implements H2ASTVisitor {
    public H2OutputVisitor(Appendable appender) {
        super(appender);
    }

    public H2OutputVisitor(Appendable appender, String dbType) {
        super(appender, dbType);
    }

    public H2OutputVisitor(Appendable appender, boolean parameterized) {
        super(appender, parameterized);
    }

    public boolean visit(SQLReplaceStatement x) {
        print0(ucase ? "MERGE INTO " : "merge into ");

        printTableSourceExpr(x.getTableName());

        List<SQLExpr> columns = x.getColumns();
        if (columns.size() > 0) {
            print0(ucase ? " KEY (" : " key (");
            for (int i = 0, size = columns.size(); i < size; ++i) {
                if (i != 0) {
                    print0(", ");
                }

                SQLExpr columnn = columns.get(i);
                printExpr(columnn);
            }
            print(')');
        }

        List<SQLInsertStatement.ValuesClause> valuesClauseList = x.getValuesList();
        if (valuesClauseList.size() != 0) {
            println();
            print0(ucase ? "VALUES " : "values ");
            int size = valuesClauseList.size();
            if (size == 0) {
                print0("()");
            } else {
                for (int i = 0; i < size; ++i) {
                    if (i != 0) {
                        print0(", ");
                    }
                    visit(valuesClauseList.get(i));
                }
            }
        }

        SQLQueryExpr query = x.getQuery();
        if (query != null) {
            visit(query);
        }

        return false;
    }
}
