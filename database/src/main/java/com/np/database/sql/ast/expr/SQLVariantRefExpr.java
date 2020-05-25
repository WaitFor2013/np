
package com.np.database.sql.ast.expr;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.visitor.SQLASTVisitor;

public class SQLVariantRefExpr extends SQLExprImpl {

    private String  name;

    private boolean global = false;

    private boolean session = false;

    private int     index  = -1;

    public SQLVariantRefExpr(String name){
        this.name = name;
    }

    public SQLVariantRefExpr(String name, boolean global){
        this.name = name;
        this.global = global;
    }

    public SQLVariantRefExpr(String name, boolean global,boolean session){
        this.name = name;
        this.global = global;
        this.session = session;
    }

    public SQLVariantRefExpr(){

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void output(StringBuffer buf) {
        buf.append(this.name);
    }


    public boolean isSession() {
        return session;
    }

    public void setSession(boolean session) {
        this.session = session;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        visitor.visit(this);

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SQLVariantRefExpr)) {
            return false;
        }
        SQLVariantRefExpr other = (SQLVariantRefExpr) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public boolean isGlobal() {
        return global;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public SQLVariantRefExpr clone() {
        SQLVariantRefExpr var =  new SQLVariantRefExpr(name, global);
        var.index = index;

        if (attributes != null) {
            var.attributes = new HashMap<String, Object>(attributes.size());
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                String k = entry.getKey();
                Object v = entry.getValue();

                if (v instanceof SQLObject) {
                    var.attributes.put(k, ((SQLObject) v).clone());
                } else {
                    var.attributes.put(k, v);
                }
            }
        }

        return var;
    }

    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
