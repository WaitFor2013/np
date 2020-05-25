
package com.np.database.sql.ast.expr;

public enum SQLUnaryOperator {
    Plus("+"), //
    Negative("-"), //
    Not("!"), //
    Compl("~"), //
    Prior("PRIOR"), //
    ConnectByRoot("CONNECT BY"), //
    BINARY("BINARY"), //
    RAW("RAW"), //
    NOT("NOT"),
    Pound("#") // Number of points in path or polygon
    ;

    public final String name;

    SQLUnaryOperator(String name){
        this.name = name;
    }
}
