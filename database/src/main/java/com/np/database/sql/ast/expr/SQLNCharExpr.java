
package com.np.database.sql.ast.expr;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.statement.SQLCharacterDataType;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.statement.SQLCharacterDataType;

public class SQLNCharExpr extends SQLTextLiteralExpr {
    private static SQLDataType defaultDataType = new SQLCharacterDataType("nvarchar");

    public SQLNCharExpr(){

    }

    public SQLNCharExpr(String text){
        super(text);
    }

    public void output(StringBuffer buf) {
        if ((this.text == null) || (this.text.length() == 0)) {
            buf.append("NULL");
            return;
        }

        buf.append("N'");
        buf.append(this.text.replaceAll("'", "''"));
        buf.append("'");
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public SQLNCharExpr clone() {
        return new SQLNCharExpr(text);
    }

    public SQLDataType computeDataType() {
        return defaultDataType;
    }
}
