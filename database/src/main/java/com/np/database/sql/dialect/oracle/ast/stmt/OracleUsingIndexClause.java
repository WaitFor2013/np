
package com.np.database.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLPartition;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObject;
import com.np.database.sql.dialect.oracle.ast.OracleSegmentAttributesImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLPartition;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleUsingIndexClause extends OracleSegmentAttributesImpl implements OracleSQLObject {

    private SQLName index;

    private Boolean             enable            = null;

    private boolean             computeStatistics = false;
    private boolean             reverse;

    private List<SQLPartition> localPartitionIndex = new ArrayList<SQLPartition>();

    public OracleUsingIndexClause(){

    }

    protected void accept0(SQLASTVisitor visitor) {
        accept0((OracleASTVisitor) visitor);
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, index);
            acceptChild(visitor, tablespace);
            acceptChild(visitor, storage);
        }
        visitor.endVisit(this);
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public boolean isComputeStatistics() {
        return computeStatistics;
    }

    public void setComputeStatistics(boolean computeStatistics) {
        this.computeStatistics = computeStatistics;
    }

    public SQLName getIndex() {
        return index;
    }

    public void setIndex(SQLName index) {
        this.index = index;
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    public List<SQLPartition> getLocalPartitionIndex() {
        return localPartitionIndex;
    }

    public void cloneTo(OracleUsingIndexClause x) {
        super.cloneTo(x);
        if (index != null) {
            x.setIndex(index.clone());
        }
        x.enable = enable;
        x.computeStatistics = computeStatistics;
        x.reverse = reverse;

        for (SQLPartition p : localPartitionIndex) {
            SQLPartition p2 = p.clone();
            p2.setParent(x);
            x.localPartitionIndex.add(p2);
        }
    }

    public OracleUsingIndexClause clone() {
        OracleUsingIndexClause x = new OracleUsingIndexClause();
        cloneTo(x);
        return x;
    }
}
