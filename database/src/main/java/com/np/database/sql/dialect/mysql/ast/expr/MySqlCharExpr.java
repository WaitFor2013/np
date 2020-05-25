
package com.np.database.sql.dialect.mysql.ast.expr;

import com.np.database.sql.ast.expr.SQLCharExpr;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class MySqlCharExpr extends SQLCharExpr implements MySqlExpr {

    private String charset;
    private String collate;

    public MySqlCharExpr(){

    }

    public MySqlCharExpr(String text){
        super(text);
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public void output(StringBuffer buf) {
        if (charset != null) {
            buf.append(charset);
            buf.append(' ');
        }
        if (super.text!=null){
            super.output(buf);
        }

        if (collate != null) {
            buf.append(" COLLATE ");
            buf.append(collate);
        }
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            accept0((MySqlASTVisitor) visitor);
        } else {
            visitor.visit(this);
            visitor.endVisit(this);
        }
    }

    public void accept0(MySqlASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    public String toString() {
        StringBuffer buf = new StringBuffer();
        output(buf);
        return buf.toString();
    }
    
    public MySqlCharExpr clone() {
    	MySqlCharExpr x = new MySqlCharExpr(text);
        x.setCharset(charset);
        x.setCollate(collate);
        return x;
    }
}
