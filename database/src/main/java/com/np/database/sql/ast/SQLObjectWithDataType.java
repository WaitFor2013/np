
package com.np.database.sql.ast;

public interface SQLObjectWithDataType extends SQLObject {
    SQLDataType getDataType();
    void setDataType(SQLDataType dataType);
}
