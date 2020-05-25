
package com.np.database.sql.dialect.mysql.ast.expr;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLExprImpl;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class MySqlMatchAgainstExpr extends SQLExprImpl implements MySqlExpr {

    private List<SQLExpr>  columns = new ArrayList<SQLExpr>();

    private SQLExpr        against;

    private SearchModifier searchModifier;

    public MySqlMatchAgainstExpr clone() {
        MySqlMatchAgainstExpr x = new MySqlMatchAgainstExpr();
        for (SQLExpr column : columns) {
            SQLExpr column2 = column.clone();
            column2.setParent(x);
            x.columns.add(column2);
        }
        if (against != null) {
            x.setAgainst(against.clone());
        }
        x.searchModifier = searchModifier;
        return x;
    }

    public List<SQLExpr> getColumns() {
        return columns;
    }

    public void setColumns(List<SQLExpr> columns) {
        this.columns = columns;
    }

    public SQLExpr getAgainst() {
        return against;
    }

    public void setAgainst(SQLExpr against) {
        if (against != null) {
            against.setParent(this);
        }
        this.against = against;
    }

    public SearchModifier getSearchModifier() {
        return searchModifier;
    }

    public void setSearchModifier(SearchModifier searchModifier) {
        this.searchModifier = searchModifier;
    }

    public static enum SearchModifier {
        IN_BOOLEAN_MODE("IN BOOLEAN MODE"), // 
        IN_NATURAL_LANGUAGE_MODE("IN NATURAL LANGUAGE MODE"), //
        IN_NATURAL_LANGUAGE_MODE_WITH_QUERY_EXPANSION("IN NATURAL LANGUAGE MODE WITH QUERY EXPANSION"),
        WITH_QUERY_EXPANSION("WITH QUERY EXPANSION");

        public final String name;
        public final String name_lcase;

        SearchModifier(){
            this(null);
        }

        SearchModifier(String name){
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        MySqlASTVisitor mysqlVisitor = (MySqlASTVisitor) visitor;
        if (mysqlVisitor.visit(this)) {
            acceptChild(visitor, this.columns);
            acceptChild(visitor, this.against);
        }
        mysqlVisitor.endVisit(this);
    }

    @Override
    public List getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        children.addAll(this.columns);
        children.add(this.against);
        return children;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((against == null) ? 0 : against.hashCode());
        result = prime * result + ((columns == null) ? 0 : columns.hashCode());
        result = prime * result + ((searchModifier == null) ? 0 : searchModifier.hashCode());
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        MySqlMatchAgainstExpr other = (MySqlMatchAgainstExpr) obj;
        if (against == null) {
            if (other.against != null) {
                return false;
            }
        } else if (!against.equals(other.against)) {
            return false;
        }
        if (columns == null) {
            if (other.columns != null) {
                return false;
            }
        } else if (!columns.equals(other.columns)) {
            return false;
        }
        if (searchModifier != other.searchModifier) {
            return false;
        }
        return true;
    }

}
