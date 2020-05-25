
package com.np.database.sql.ast.expr;


import com.np.database.sql.util.FnvHash;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.util.FnvHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLAggregateExpr extends com.np.database.sql.ast.expr.SQLMethodInvokeExpr implements Serializable, com.np.database.sql.ast.SQLReplaceable {

    private static final long     serialVersionUID = 1L;

    protected SQLAggregateOption option;

    protected com.np.database.sql.ast.SQLKeep keep;
    protected com.np.database.sql.ast.SQLExpr filter;
    protected com.np.database.sql.ast.SQLOver over;
    protected com.np.database.sql.ast.SQLName overRef;
    protected com.np.database.sql.ast.SQLOrderBy withinGroup;
    protected Boolean             ignoreNulls      = false;

    public SQLAggregateExpr(String methodName){
        this.methodName = methodName;
    }
    public SQLAggregateExpr(String methodName, SQLAggregateOption option){
        this.methodName = methodName;
        this.option = option;
    }

    public SQLAggregateExpr(String methodName, SQLAggregateOption option, com.np.database.sql.ast.SQLExpr... arguments){
        this.methodName = methodName;
        this.option = option;
        if (arguments != null) {
            for (com.np.database.sql.ast.SQLExpr argument : arguments) {
                if (argument != null) {
                    addArgument(argument);
                }
            }
        }
    }

    public com.np.database.sql.ast.SQLOrderBy getWithinGroup() {
        return withinGroup;
    }

    public void setWithinGroup(com.np.database.sql.ast.SQLOrderBy withinGroup) {
        if (withinGroup != null) {
            withinGroup.setParent(this);
        }

        this.withinGroup = withinGroup;
    }

    public SQLAggregateOption getOption() {
        return this.option;
    }

    public void setOption(SQLAggregateOption option) {
        this.option = option;
    }

    public boolean isDistinct() {
        return option == com.np.database.sql.ast.expr.SQLAggregateOption.DISTINCT;
    }

    public com.np.database.sql.ast.SQLOver getOver() {
        return over;
    }

    public void setOver(com.np.database.sql.ast.SQLOver x) {
        if (x != null) {
            x.setParent(this);
        }
        this.over = x;
    }

    public com.np.database.sql.ast.SQLName getOverRef() {
        return overRef;
    }

    public void setOverRef(com.np.database.sql.ast.SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.overRef = x;
    }

    public com.np.database.sql.ast.SQLKeep getKeep() {
        return keep;
    }

    public void setKeep(com.np.database.sql.ast.SQLKeep keep) {
        if (keep != null) {
            keep.setParent(this);
        }
        this.keep = keep;
    }

    public boolean isIgnoreNulls() {
        return this.ignoreNulls != null && this.ignoreNulls;
    }

    public Boolean getIgnoreNulls() {
        return this.ignoreNulls;
    }

    public void setIgnoreNulls(boolean ignoreNulls) {
        this.ignoreNulls = ignoreNulls;
    }

    public String toString() {
        return NpSqlHelper.toSQLString(this);
    }


    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.owner != null) {
                this.owner.accept(visitor);
            }

            for (com.np.database.sql.ast.SQLExpr arg : this.arguments) {
                if (arg != null) {
                    arg.accept(visitor);
                }
            }

            if (this.keep != null) {
                this.keep.accept(visitor);
            }

            if (this.filter != null) {
                this.filter.accept(visitor);
            }

            if (this.over != null) {
                this.over.accept(visitor);
            }

            if (this.overRef != null) {
                this.overRef.accept(visitor);
            }

            if (this.withinGroup != null) {
                this.withinGroup.accept(visitor);
            }
        }

        visitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        List<com.np.database.sql.ast.SQLObject> children = new ArrayList<com.np.database.sql.ast.SQLObject>();
        children.addAll(this.arguments);
        if (keep != null) {
            children.add(this.keep);
        }
        if (over != null) {
            children.add(over);
        }
        if (withinGroup != null) {
            children.add(withinGroup);
        }
        return children;
    }

    public com.np.database.sql.ast.SQLExpr getFilter() {
        return filter;
    }

    public void setFilter(com.np.database.sql.ast.SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }

        this.filter = x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SQLAggregateExpr that = (SQLAggregateExpr) o;

        if (option != that.option) return false;
        if (keep != null ? !keep.equals(that.keep) : that.keep != null) return false;
        if (filter != null ? !filter.equals(that.filter) : that.filter != null) return false;
        if (over != null ? !over.equals(that.over) : that.over != null) return false;
        if (overRef != null ? !overRef.equals(that.overRef) : that.overRef != null) return false;
        if (withinGroup != null ? !withinGroup.equals(that.withinGroup) : that.withinGroup != null) return false;
        return ignoreNulls != null ? ignoreNulls.equals(that.ignoreNulls) : that.ignoreNulls == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (option != null ? option.hashCode() : 0);
        result = 31 * result + (keep != null ? keep.hashCode() : 0);
        result = 31 * result + (filter != null ? filter.hashCode() : 0);
        result = 31 * result + (over != null ? over.hashCode() : 0);
        result = 31 * result + (overRef != null ? overRef.hashCode() : 0);
        result = 31 * result + (withinGroup != null ? withinGroup.hashCode() : 0);
        result = 31 * result + (ignoreNulls != null ? ignoreNulls.hashCode() : 0);
        return result;
    }

    public SQLAggregateExpr clone() {
        SQLAggregateExpr x = new SQLAggregateExpr(methodName);

        x.option = option;

        for (com.np.database.sql.ast.SQLExpr arg : arguments) {
            x.addArgument(arg.clone());
        }

        if (keep != null) {
            x.setKeep(keep.clone());
        }

        if (over != null) {
            x.setOver(over.clone());
        }

        if (overRef != null) {
            x.setOverRef(overRef.clone());
        }

        if (withinGroup != null) {
            x.setWithinGroup(withinGroup.clone());
        }

        x.ignoreNulls = ignoreNulls;

        if (attributes != null) {
            for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof com.np.database.sql.ast.SQLObject) {
                    value = ((com.np.database.sql.ast.SQLObject) value).clone();
                }
                x.putAttribute(key, value);
            }
        }

        return x;
    }

    public com.np.database.sql.ast.SQLDataType computeDataType() {
        if (resolvedReturnDataType != null) {
            return resolvedReturnDataType;
        }

        long hash = methodNameHashCode64();

        if (arguments.size() > 0) {
            com.np.database.sql.ast.SQLDataType dataType = arguments.get(0)
                    .computeDataType();
            if (dataType != null
                    && (dataType.nameHashCode64() != FnvHash.Constants.BOOLEAN)) {
                return dataType;
            }
        }

        return null;
    }

    public boolean replace(com.np.database.sql.ast.SQLExpr expr, com.np.database.sql.ast.SQLExpr target) {
        if (target == null) {
            return false;
        }

        for (int i = 0; i < arguments.size(); ++i) {
            if (arguments.get(i) == expr) {
                arguments.set(i, target);
                target.setParent(this);
                return true;
            }
        }

        if (overRef == expr) {
            setOverRef((com.np.database.sql.ast.SQLName) target);
            return true;
        }

        if (filter != null) {
            filter = target;
            target.setParent(this);
        }

        return false;
    }
}
