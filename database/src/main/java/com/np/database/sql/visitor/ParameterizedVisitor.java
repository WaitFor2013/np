
package com.np.database.sql.visitor;

import java.util.List;

public interface ParameterizedVisitor extends PrintableVisitor {

    int getReplaceCount();

    void incrementReplaceCunt();

    String getDbType();

    void setOutputParameters(List<Object> parameters);

    void config(VisitorFeature feature, boolean state);
}
