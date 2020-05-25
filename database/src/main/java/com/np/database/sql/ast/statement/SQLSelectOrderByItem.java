
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.ast.SQLOrderingSpecification;
import com.np.database.sql.ast.SQLReplaceable;
import com.np.database.sql.visitor.SQLASTVisitor;

import java.util.List;

public final class SQLSelectOrderByItem extends SQLObjectImpl implements SQLReplaceable {

    protected SQLExpr                  expr;
    protected String                   collate;
    protected SQLOrderingSpecification type;
    protected NullsOrderType           nullsOrderType;

    protected transient SQLSelectItem  resolvedSelectItem;

    public SQLSelectOrderByItem(){

    }

    public SQLSelectOrderByItem(SQLExpr expr){
        this.setExpr(expr);
    }

    public SQLSelectOrderByItem(SQLExpr expr, SQLOrderingSpecification type){
        this.setExpr(expr);
        this.type = type;
    }

    public SQLExpr getExpr() {
        return this.expr;
    }

    public void setExpr(SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }

    public String getCollate() {
        return collate;
    }

    public void setCollate(String collate) {
        this.collate = collate;
    }

    public SQLOrderingSpecification getType() {
        return this.type;
    }

    public void setType(SQLOrderingSpecification type) {
        this.type = type;
    }
    
    public NullsOrderType getNullsOrderType() {
        return this.nullsOrderType;
    }

    public void setNullsOrderType(NullsOrderType nullsOrderType) {
        this.nullsOrderType = nullsOrderType;
    }

    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.expr);
        }

        visitor.endVisit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((collate == null) ? 0 : collate.hashCode());
        result = prime * result + ((expr == null) ? 0 : expr.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SQLSelectOrderByItem other = (SQLSelectOrderByItem) obj;
        if (collate == null) {
            if (other.collate != null) return false;
        } else if (!collate.equals(other.collate)) return false;
        if (expr == null) {
            if (other.expr != null) return false;
        } else if (!expr.equals(other.expr)) return false;
        if (type != other.type) return false;
        return true;
    }

    @Override
    public boolean replace(SQLExpr expr, SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        return false;
    }

    public static enum NullsOrderType {
        NullsFirst, NullsLast;

        public String toFormalString() {
            if (NullsFirst.equals(this)) {
                return "NULLS FIRST";
            }

            if (NullsLast.equals(this)) {
                return "NULLS LAST";
            }

            throw new IllegalArgumentException();
        }
    }

    public SQLSelectOrderByItem clone() {
        SQLSelectOrderByItem x = new SQLSelectOrderByItem();
        if (expr != null) {
            x.setExpr(expr.clone());
        }
        x.collate = collate;
        x.type = type;
        x.nullsOrderType = nullsOrderType;
        return x;
    }

    public SQLSelectItem getResolvedSelectItem() {
        return resolvedSelectItem;
    }

    public void setResolvedSelectItem(SQLSelectItem resolvedSelectItem) {
        this.resolvedSelectItem = resolvedSelectItem;
    }

    public boolean isClusterBy() {
        if (parent instanceof SQLCreateTableStatement) {
            List<SQLSelectOrderByItem> clusteredBy = ((SQLCreateTableStatement) parent).getClusteredBy();
            return clusteredBy.indexOf(this) != -1;
        }

        if (parent instanceof SQLSelectQueryBlock) {
            List<SQLSelectOrderByItem> clusterBy = ((SQLSelectQueryBlock) parent).getClusterByDirect();
            return clusterBy != null && clusterBy.indexOf(this) != -1;
        }

        return false;
    }

    public boolean isSortBy() {
        if (parent instanceof SQLCreateTableStatement) {
            List<SQLSelectOrderByItem> sortedBy = ((SQLCreateTableStatement) parent).getSortedBy();
            return sortedBy.indexOf(this) != -1;
        }

        if (parent instanceof SQLSelectQueryBlock) {
            List<SQLSelectOrderByItem> sortedBy = ((SQLSelectQueryBlock) parent).getSortByDirect();
            return sortedBy != null && sortedBy.indexOf(this) != -1;
        }

        return false;
    }

    public boolean isDistributeBy() {
        if (parent instanceof SQLSelectQueryBlock) {
            List<SQLSelectOrderByItem> distributeBy = ((SQLSelectQueryBlock) parent).getDistributeBy();
            return distributeBy.indexOf(this) != -1;
        }

        return false;
    }
}
