
package com.np.database.sql.dialect.oracle.ast.expr;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLOver;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLDataType;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLOver;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleAnalytic extends SQLOver implements OracleExpr {

    private OracleAnalyticWindowing windowing;

    public OracleAnalytic(){

    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, this.partitionBy);
            acceptChild(visitor, this.orderBy);
            acceptChild(visitor, this.windowing);
        }
        visitor.endVisit(this);
    }

    @Override
    public List<SQLObject> getChildren() {
        List<SQLObject> children = new ArrayList<SQLObject>();
        children.addAll(this.partitionBy);
        if (this.orderBy != null) {
            children.add(orderBy);
        }
        if (this.windowing != null) {
            children.add(windowing);
        }
        return children;
    }

    public OracleAnalyticWindowing getWindowing() {
        return this.windowing;
    }

    public OracleAnalytic clone() {
        OracleAnalytic x = new OracleAnalytic();

        cloneTo(x);

        if (windowing != null) {
            x.setWindowing(windowing.clone());
        }

        return x;
    }

    public void setWindowing(OracleAnalyticWindowing windowing) {
        this.windowing = windowing;
    }

    public SQLDataType computeDataType() {
        return null;
    }
}
