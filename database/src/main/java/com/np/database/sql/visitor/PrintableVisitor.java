
package com.np.database.sql.visitor;

public interface PrintableVisitor extends SQLASTVisitor {
    boolean isUppCase();

    void print(char value);

    void print(String text);
}
