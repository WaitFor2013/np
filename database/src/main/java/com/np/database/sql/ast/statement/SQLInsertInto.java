
package com.np.database.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObjectImpl;
import com.np.database.sql.ast.statement.SQLInsertStatement.ValuesClause;

public abstract class SQLInsertInto extends SQLObjectImpl {
    protected SQLExprTableSource        tableSource;
    protected final List<SQLExpr>       columns = new ArrayList<SQLExpr>();
    protected transient String          columnsString;
    protected transient long            columnsStringHash;
    protected SQLSelect                 query;
    protected final List<SQLInsertStatement.ValuesClause>  valuesList = new ArrayList<SQLInsertStatement.ValuesClause>();

    public SQLInsertInto(){

    }

    public void cloneTo(SQLInsertInto x) {
        if (tableSource != null) {
            x.setTableSource(tableSource.clone());
        }
        for (SQLExpr column : columns) {
            SQLExpr column2 = column.clone();
            column2.setParent(x);
            x.columns.add(column2);
        }
        if (query != null) {
            x.setQuery(query.clone());
        }
        for (SQLInsertStatement.ValuesClause v : valuesList) {
            SQLInsertStatement.ValuesClause v2 = v.clone();
            v2.setParent(x);
            x.valuesList.add(v2);
        }
    }

    public abstract SQLInsertInto clone();

    public String getAlias() {
        return tableSource.getAlias();
    }

    public void setAlias(String alias) {
        this.tableSource.setAlias(alias);
    }

    public SQLExprTableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSource = tableSource;
    }

    public SQLName getTableName() {
        return (SQLName) tableSource.getExpr();
    }

    public void setTableName(SQLName tableName) {
        this.setTableSource(new SQLExprTableSource(tableName));
    }

    public void setTableSource(SQLName tableName) {
        this.setTableSource(new SQLExprTableSource(tableName));
    }

    public SQLSelect getQuery() {
        return query;
    }

    public void setQuery(SQLSelectQuery query) {
        this.setQuery(new SQLSelect(query));
    }

    public void setQuery(SQLSelect query) {
        if (query != null) {
            query.setParent(this);
        }
        this.query = query;
    }

    public List<SQLExpr> getColumns() {
        return columns;
    }
    
    public void addColumn(SQLExpr column) {
        if (column != null) {
            column.setParent(this);
        }
        this.columns.add(column);
    }

    public SQLInsertStatement.ValuesClause getValues() {
        if (valuesList.size() == 0) {
            return null;
        }
        return valuesList.get(0);
    }

    public void setValues(SQLInsertStatement.ValuesClause values) {
        if (valuesList.size() == 0) {
            valuesList.add(values);
        } else {
            valuesList.set(0, values);
        }
    }
    
    public List<SQLInsertStatement.ValuesClause> getValuesList() {
        return valuesList;
    }

    public void addValueCause(SQLInsertStatement.ValuesClause valueClause) {
        if (valueClause != null) {
            valueClause.setParent(this);
        }
        valuesList.add(valueClause);
    }

    public String getColumnsString() {
        return columnsString;
    }

    public long getColumnsStringHash() {
        return columnsStringHash;
    }

    public void setColumnsString(String columnsString, long columnsStringHash) {
        this.columnsString = columnsString;
        this.columnsStringHash = columnsStringHash;
    }
}
