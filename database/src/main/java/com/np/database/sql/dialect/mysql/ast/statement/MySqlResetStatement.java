
package com.np.database.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlResetStatement extends MySqlStatementImpl {

    private List<String> options = new ArrayList<String>();

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
}
