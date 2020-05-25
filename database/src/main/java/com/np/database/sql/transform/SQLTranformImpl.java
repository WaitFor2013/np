
package com.np.database.sql.transform;

public abstract class SQLTranformImpl implements SQLTranform {
    protected String sourceDbType;
    protected String targetDbType;

    @Override
    public String getSourceDbType() {
        return sourceDbType;
    }

    @Override
    public String getTargetDbType() {
        return targetDbType;
    }
}
