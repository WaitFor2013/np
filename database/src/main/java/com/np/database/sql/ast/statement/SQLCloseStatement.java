
package com.np.database.sql.ast.statement;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

/**
 * 
 * MySql cursor close statement
 * @author zz [455910092@qq.com]
 */
public class SQLCloseStatement extends SQLStatementImpl{
	
	//cursor name
	private SQLName cursorName;
	
	public SQLName getCursorName() {
		return cursorName;
	}

	public void setCursorName(String cursorName) {
		setCursorName(new SQLIdentifierExpr(cursorName));
	}
	
	public void setCursorName(SQLName cursorName) {
		if (cursorName != null) {
			cursorName.setParent(this);
		}
		this.cursorName = cursorName;
	}

	@Override
	protected void accept0(SQLASTVisitor visitor) {
		if (visitor.visit(this)) {
			acceptChild(visitor, cursorName);
		}
	    visitor.endVisit(this);
		
	}

	@Override
	public List<SQLObject> getChildren() {
		return Collections.<SQLObject>emptyList();
	}
}
