
package com.np.database.sql.dialect.mysql.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLIfStatement;
import com.np.database.sql.dialect.mysql.ast.MySqlObjectImpl;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
/**
 * 
 * @author zz [455910092@qq.com]
 */
public class MySqlCaseStatement extends MySqlStatementImpl{

	//case expr
	private SQLExpr            		  condition;
	//when statement list
	private List<MySqlWhenStatement> whenList = new ArrayList<MySqlCaseStatement.MySqlWhenStatement>();
	//else statement
	private SQLIfStatement.Else        elseItem;
	
	public SQLExpr getCondition() {
		return condition;
	}

	public void setCondition(SQLExpr condition) {
		this.condition = condition;
	}

	public List<MySqlWhenStatement> getWhenList() {
		return whenList;
	}

	public void setWhenList(List<MySqlWhenStatement> whenList) {
		this.whenList = whenList;
	}
	
	public void addWhenStatement(MySqlWhenStatement stmt)
	{
		this.whenList.add(stmt);
	}

	public SQLIfStatement.Else getElseItem() {
		return elseItem;
	}

	public void setElseItem(SQLIfStatement.Else elseItem) {
		this.elseItem = elseItem;
	}

	@Override
	public void accept0(MySqlASTVisitor visitor) {
		// TODO Auto-generated method stub
		if (visitor.visit(this)) {
            acceptChild(visitor, condition);
            acceptChild(visitor, whenList);
            acceptChild(visitor, elseItem);
        }
        visitor.endVisit(this);
	}

	@Override
	public List<SQLObject> getChildren() {
		List<SQLObject> children = new ArrayList<SQLObject>();
		children.addAll(children);
		children.addAll(whenList);
		children.addAll(whenList);
		if (elseItem != null) {
			children.add(elseItem);
		}
		return children;
	}

	/**
	 * case when statement
	 * @author zz
	 *
	 */
	public static class MySqlWhenStatement extends MySqlObjectImpl {

        private SQLExpr            condition;
        private List<SQLStatement> statements = new ArrayList<SQLStatement>();

        @Override
        public void accept0(MySqlASTVisitor visitor) {
            if (visitor.visit(this)) {
                acceptChild(visitor, condition);
                acceptChild(visitor, statements);
            }
            visitor.endVisit(this);
        }

        public SQLExpr getCondition() {
            return condition;
        }

        public void setCondition(SQLExpr condition) {
            this.condition = condition;
        }

        public List<SQLStatement> getStatements() {
            return statements;
        }

        public void setStatements(List<SQLStatement> statements) {
            this.statements = statements;
        }

    }

}
