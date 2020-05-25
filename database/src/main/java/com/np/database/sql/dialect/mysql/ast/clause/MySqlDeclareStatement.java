
package com.np.database.sql.dialect.mysql.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLDeclareItem;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * @author zz [455910092@qq.com]
 */
public class MySqlDeclareStatement extends MySqlStatementImpl {

    private List<SQLDeclareItem> varList = new ArrayList<SQLDeclareItem>();

    public List<SQLDeclareItem> getVarList() {
        return varList;
    }

    public void addVar(SQLDeclareItem expr) {
        varList.add(expr);
    }

    public void setVarList(List<SQLDeclareItem> varList) {
        this.varList = varList;
    }

    @Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, varList);
        }
        visitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        return varList;
    }
}
