
package com.np.database.sql.visitor;

/**
 * Created by wenshao on 16/07/2017.
 */
public interface SQLTransformVisitor extends SQLASTVisitor {
    String getSrcDbType();
}
