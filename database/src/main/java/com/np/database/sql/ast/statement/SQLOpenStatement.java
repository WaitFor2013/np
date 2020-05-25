
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLStatementImpl;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

/**
 * 
 * @author zz [455910092@qq.com]
 */
public class SQLOpenStatement extends SQLStatementImpl{
	
	//cursor name
	private SQLName cursorName;

	private final List<SQLName> columns = new ArrayList<SQLName>();

	private SQLExpr forExpr;

	public SQLOpenStatement() {

	}
	
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
			acceptChild(visitor, forExpr);
			acceptChild(visitor, columns);
		}
	    visitor.endVisit(this);
	}

	public SQLExpr getFor() {
		return forExpr;
	}

	public void setFor(SQLExpr forExpr) {
		if (forExpr != null) {
			forExpr.setParent(this);
		}
		this.forExpr = forExpr;
	}

	public List<SQLName> getColumns() {
		return columns;
	}
}
