
package com.np.database.sql.ast.statement;

import java.util.List;

import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLCommentHint;
import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.expr.SQLIntegerExpr;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLCharacterDataType extends SQLDataTypeImpl {

    private String             charSetName;
    private String             collate;

    private String             charType;
    private boolean            hasBinary;

    public List<SQLCommentHint> hints;

    public final static String CHAR_TYPE_BYTE = "BYTE";
    public final static String CHAR_TYPE_CHAR = "CHAR";

    public SQLCharacterDataType(String name){
        super(name);
    }

    public SQLCharacterDataType(String name, int precision){
        super(name, precision);
    }

    public String getCharSetName() {
        return charSetName;
    }

    public void setCharSetName(String charSetName) {
        this.charSetName = charSetName;
    }
    
    public boolean isHasBinary() {
        return hasBinary;
    }

    public void setHasBinary(boolean hasBinary) {
        this.hasBinary = hasBinary;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public String getCharType() {
        return charType;
    }

    public void setCharType(String charType) {
        this.charType = charType;
    }

    public List<SQLCommentHint> getHints() {
        return hints;
    }

    public void setHints(List<SQLCommentHint> hints) {
        this.hints = hints;
    }

    public int getLength() {
        if (this.arguments.size() == 1) {
            SQLExpr arg = this.arguments.get(0);
            if (arg instanceof SQLIntegerExpr) {
                return ((SQLIntegerExpr) arg).getNumber().intValue();
            }
        }

        return -1;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.arguments);
        }

        visitor.endVisit(this);
    }


    public SQLCharacterDataType clone() {
        SQLCharacterDataType x = new SQLCharacterDataType(getName());

        super.cloneTo(x);

        x.charSetName = charSetName;
        x.collate = collate;
        x.charType = charType;
        x.hasBinary = hasBinary;

        return x;
    }

    @Override
    public String toString() {
        return NpSqlHelper.toSQLString(this);
    }
}
