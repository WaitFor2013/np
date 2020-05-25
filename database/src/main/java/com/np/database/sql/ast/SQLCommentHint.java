
package com.np.database.sql.ast;

import com.np.database.sql.visitor.SQLASTOutputVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLCommentHint extends SQLObjectImpl implements SQLHint {

    private String text;

    public SQLCommentHint(){

    }

    public SQLCommentHint(String text){

        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    public SQLCommentHint clone() {
        return new SQLCommentHint(text);
    }

    public void output(StringBuffer buf) {
        new SQLASTOutputVisitor(buf).visit(this);
    }
}
