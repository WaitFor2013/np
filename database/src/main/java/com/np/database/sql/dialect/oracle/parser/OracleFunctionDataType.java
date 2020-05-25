
package com.np.database.sql.dialect.oracle.parser;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.ast.SQLParameter;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLDataTypeImpl;
import com.np.database.sql.ast.SQLParameter;
import com.np.database.sql.ast.SQLStatement;

public class OracleFunctionDataType extends SQLDataTypeImpl {
    private boolean isStatic = false;
    private final List<SQLParameter> parameters = new ArrayList<SQLParameter>();

    private SQLDataType returnDataType;

    private SQLStatement block;

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    public List<SQLParameter> getParameters() {
        return parameters;
    }

    public SQLDataType getReturnDataType() {
        return returnDataType;
    }

    public void setReturnDataType(SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.returnDataType = x;
    }

    public OracleFunctionDataType clone() {
        OracleFunctionDataType x = new OracleFunctionDataType();
        cloneTo(x);

        x.isStatic = isStatic;
        if (returnDataType != null) {
            x.setReturnDataType(returnDataType.clone());
        }
        for (SQLParameter parameter : parameters) {
            SQLParameter p2 = parameter.clone();
            p2.setParent(x);
            x.parameters.add(p2);
        }

        return x;
    }

    public SQLStatement getBlock() {
        return block;
    }

    public void setBlock(SQLStatement block) {
        if (block != null) {
            block.setParent(this);
        }
        this.block = block;
    }
}
