
package com.np.database.sql.dialect.mysql.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;

/**
 * 
 * @author zz [455910092@qq.com]
 */
public class MySqlRepeatStatement extends MySqlStatementImpl {
	
	private String labelName;

	private List<SQLStatement> statements = new ArrayList<SQLStatement>();
	
	private SQLExpr            condition;
	
	@Override
    public void accept0(MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, statements);
            acceptChild(visitor, condition);
        }
        visitor.endVisit(this);
    }

    public List<SQLStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<SQLStatement> statements) {
        this.statements = statements;
    }

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
    
	public SQLExpr getCondition() {
		return condition;
	}

	public void setCondition(SQLExpr condition) {
		this.condition = condition;
	}
}
