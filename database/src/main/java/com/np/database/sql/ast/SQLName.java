
package com.np.database.sql.ast;

public interface SQLName extends SQLExpr {
    String  getSimpleName();
    SQLName clone();
    long    nameHashCode64();
    long    hashCode64();
}
