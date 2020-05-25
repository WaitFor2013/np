
package com.np.database.sql.visitor;

import java.util.List;


public interface ExportParameterVisitor extends SQLASTVisitor {
    boolean isParameterizedMergeInList();
    void setParameterizedMergeInList(boolean flag);

    List<Object> getParameters();
}
