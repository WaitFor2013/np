
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author zz [455910092@qq.com]
 */
public class SQLWhileStatement extends SQLStatementImpl {
	
	//while expr
	private SQLExpr            condition;
	private List<SQLStatement> statements = new ArrayList<SQLStatement>();
	//while label name
	private String labelName;
	
    
	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

    public void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
        	acceptChild(visitor, condition);
            acceptChild(visitor, statements);
        }
        visitor.endVisit(this);
    }

	@Override
	public List<SQLObject> getChildren() {
		List<SQLObject> children = new ArrayList<SQLObject>();
		children.add(condition);
		children.addAll(this.statements);
		return children;
	}

	public List<SQLStatement> getStatements() {
        return statements;
    }

    public void setStatements(List<SQLStatement> statements) {
        this.statements = statements;
    }
    public SQLExpr getCondition() {
		return condition;
	}

	public void setCondition(SQLExpr condition) {
		this.condition = condition;
	}

	public SQLWhileStatement clone() {
		SQLWhileStatement x = new SQLWhileStatement();

		if (condition != null) {
			x.setCondition(condition.clone());
		}
		for (SQLStatement stmt : statements) {
			SQLStatement stmt2 = stmt.clone();
			stmt2.setParent(x);
			x.statements.add(stmt2);
		}
		x.labelName = labelName;
		return x;
	}
}
