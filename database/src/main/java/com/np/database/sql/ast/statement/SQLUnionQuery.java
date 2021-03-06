
package com.np.database.sql.ast.statement;

import com.np.database.sql.ast.SQLDbTypedObject;
import com.np.database.sql.ast.SQLLimit;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.ast.SQLOrderBy;
import com.np.database.sql.visitor.SQLASTVisitor;

import java.util.ArrayList;
import java.util.List;

public class SQLUnionQuery extends SQLObjectImpl implements SQLSelectQuery, SQLDbTypedObject {

    private boolean          bracket  = false;

    private List<SQLSelectQuery> relations = new ArrayList<SQLSelectQuery>();
    private SQLUnionOperator operator = SQLUnionOperator.UNION;
    private SQLOrderBy       orderBy;

    private SQLLimit         limit;
    private String           dbType;

    public SQLUnionOperator getOperator() {
        return operator;
    }

    public void setOperator(SQLUnionOperator operator) {
        this.operator = operator;
    }

    public SQLUnionQuery(){

    }

    public SQLUnionQuery(SQLSelectQuery left, SQLUnionOperator operator, SQLSelectQuery right){
        this.setLeft(left);
        this.operator = operator;
        this.setRight(right);
    }


    public List<SQLSelectQuery> getRelations() {
        return relations;
    }

    public void addRelation(SQLSelectQuery relation) {
        if (relation != null) {
            relation.setParent(this);
        }

        relations.add(relation);
    }

    public SQLSelectQuery getLeft() {
        if (relations.size() == 0) {
            return null;
        }
        return relations.get(0);
    }

    public void setLeft(SQLSelectQuery left) {
        if (left != null) {
            left.setParent(this);
        }

        if (relations.size() == 0) {
            relations.add(left);
        } else if (relations.size() <= 2) {
            relations.set(0, left);
        } else {
            throw new UnsupportedOperationException("multi-union");
        }
    }

    public SQLSelectQuery getRight() {
        if (relations.size() < 2) {
            return null;
        }
        if (relations.size() == 2) {
            return relations.get(1);
        }

        throw new UnsupportedOperationException("multi-union");
    }

    public void setRight(SQLSelectQuery right) {
        if (right != null) {
            right.setParent(this);
        }

        if (relations.size() == 0) {
            relations.add(null);
        }

        if (relations.size() == 1) {
            relations.add(right);
        } else if (relations.size() == 2) {
            relations.set(1, right);
        } else {
            throw new UnsupportedOperationException("multi-union");
        }
    }

    public SQLOrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(SQLOrderBy orderBy) {
        if (orderBy != null) {
            orderBy.setParent(this);
        }
        this.orderBy = orderBy;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (SQLSelectQuery relation : relations) {
                relation.accept(visitor);
            }

            if (orderBy != null) {
                orderBy.accept(visitor);
            }

            if (limit != null) {
                limit.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }


    public SQLLimit getLimit() {
        return limit;
    }

    public void setLimit(SQLLimit limit) {
        if (limit != null) {
            limit.setParent(this);
        }
        this.limit = limit;
    }

    public boolean isBracket() {
        return bracket;
    }

    public void setBracket(boolean bracket) {
        this.bracket = bracket;
    }

    public SQLUnionQuery clone() {
        SQLUnionQuery x = new SQLUnionQuery();

        x.bracket = bracket;

        for (SQLSelectQuery relation : relations) {
            SQLSelectQuery r = relation.clone();
            r.setParent(x);
            x.relations.add(r);
        }

        x.operator = operator;

        if (orderBy != null) {
            x.setOrderBy(orderBy.clone());
        }

        if (limit != null) {
            x.setLimit(limit.clone());
        }

        x.dbType = dbType;

        return x;
    }

    public SQLSelectQueryBlock getFirstQueryBlock() {
        SQLSelectQuery left = getLeft();

        if (left instanceof SQLSelectQueryBlock) {
            return (SQLSelectQueryBlock) left;
        }

        if (left instanceof SQLUnionQuery) {
            return ((SQLUnionQuery) left).getFirstQueryBlock();
        }

        return null;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public boolean replace(SQLSelectQuery cmp, SQLSelectQuery target) {
        for (int i = 0; i < relations.size(); i++) {
            SQLSelectQuery r = relations.get(i);
            if (r == cmp) {
                if (cmp != null) {
                    cmp.setParent(this);
                }
                relations.set(i, cmp);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SQLUnionQuery that = (SQLUnionQuery) o;

        if (bracket != that.bracket) return false;
        if (relations != null ? !relations.equals(that.relations) : that.relations != null) return false;
        if (operator != that.operator) return false;
        if (orderBy != null ? !orderBy.equals(that.orderBy) : that.orderBy != null) return false;
        if (limit != null ? !limit.equals(that.limit) : that.limit != null) return false;
        return dbType == that.dbType;
    }

    @Override
    public int hashCode() {
        int result = (bracket ? 1 : 0);
        result = 31 * result + (relations != null ? relations.hashCode() : 0);
        result = 31 * result + (operator != null ? operator.hashCode() : 0);
        result = 31 * result + (orderBy != null ? orderBy.hashCode() : 0);
        result = 31 * result + (limit != null ? limit.hashCode() : 0);
        result = 31 * result + (dbType != null ? dbType.hashCode() : 0);
        return result;
    }
}
