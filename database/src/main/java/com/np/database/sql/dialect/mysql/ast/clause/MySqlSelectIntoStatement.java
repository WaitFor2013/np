
package com.np.database.sql.dialect.mysql.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.statement.SQLSelect;
import com.np.database.sql.dialect.mysql.ast.statement.MySqlStatementImpl;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
/**
 * 
 * @author zz [455910092@qq.com]
 */
public class MySqlSelectIntoStatement extends MySqlStatementImpl{

	//select statement
	private SQLSelect select;
	//var list
	private List<SQLExpr> varList=new ArrayList<SQLExpr>();
	
	public SQLSelect getSelect() {
		return select;
	}

	public void setSelect(SQLSelect select) {
		this.select = select;
	}

	public List<SQLExpr> getVarList() {
		return varList;
	}

	public void setVarList(List<SQLExpr> varList) {
		this.varList = varList;
	}

	
	
	@Override
	public void accept0(MySqlASTVisitor visitor) {
		if (visitor.visit(this)) {
            acceptChild(visitor, select);
            acceptChild(visitor, varList);
        }
        visitor.endVisit(this);
	}

}
