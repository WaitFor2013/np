
package com.np.database.sql.ast.statement;

public enum SQLUnionOperator {
    UNION("UNION"),
    UNION_ALL("UNION ALL"),
    MINUS("MINUS"), EXCEPT("EXCEPT"),
    INTERSECT("INTERSECT"),
    DISTINCT("UNION DISTINCT");

    public final String name;
    public final String name_lcase;

    private SQLUnionOperator(String name){
        this.name = name;
        this.name_lcase = name.toLowerCase();
    }

    public String toString() {
        return name;
    }
}
