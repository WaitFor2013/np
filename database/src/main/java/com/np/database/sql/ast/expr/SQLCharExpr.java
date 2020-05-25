
package com.np.database.sql.ast.expr;

import java.util.Collections;
import java.util.List;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.statement.SQLCharacterDataType;
import com.np.database.sql.visitor.SQLASTOutputVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLCharacterDataType;

public class SQLCharExpr extends SQLTextLiteralExpr implements SQLValuableExpr{
    public static final SQLDataType DEFAULT_DATA_TYPE = new SQLCharacterDataType("varchar");

    public SQLCharExpr(){

    }

    public SQLCharExpr(String text){
        super(text);
    }

    @Override
    public void output(StringBuffer buf) {
        output((Appendable) buf);
    }

    public void output(Appendable buf) {
        this.accept(new SQLASTOutputVisitor(buf));
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    @Override
    public Object getValue() {
        return this.text;
    }
    
    public String toString() {
        return NpSqlHelper.toSQLString(this);
    }

    public SQLCharExpr clone() {
        return new SQLCharExpr(this.text);
    }

    public SQLDataType computeDataType() {
        return DEFAULT_DATA_TYPE;
    }

    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
