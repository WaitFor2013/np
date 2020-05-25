
package com.np.database.sql.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;

import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.ast.OracleSQLObject;
import com.np.database.sql.dialect.oracle.ast.OracleSegmentAttributesImpl;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.np.database.sql.visitor.SQLASTVisitor;

public class OracleLobStorageClause extends OracleSegmentAttributesImpl implements OracleSQLObject {

    private SQLName segementName;

    private final List<SQLName> items      = new ArrayList<SQLName>();

    private boolean             secureFile = false;
    private boolean             basicFile  = false;


    private Boolean             enable;

    private SQLExpr chunk;

    private Boolean             cache;
    private Boolean             logging;

    private Boolean             compress;
    private Boolean             keepDuplicate;
    private boolean             retention;

    private OracleStorageClause storageClause;

    private SQLExpr             pctversion;

    protected void accept0(SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor) visitor);
    }

    @Override
    public void accept0(OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            acceptChild(visitor, segementName);
            acceptChild(visitor, items);
            acceptChild(visitor, tablespace);
        }
        visitor.endVisit(this);
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public SQLExpr getChunk() {
        return chunk;
    }

    public void setChunk(SQLExpr chunk) {
        this.chunk = chunk;
    }

    public List<SQLName> getItems() {
        return items;
    }

    public boolean isSecureFile() {
        return secureFile;
    }

    public void setSecureFile(boolean secureFile) {
        this.secureFile = secureFile;
    }

    public boolean isBasicFile() {
        return basicFile;
    }

    public void setBasicFile(boolean basicFile) {
        this.basicFile = basicFile;
    }

    public Boolean getCache() {
        return cache;
    }

    public void setCache(Boolean cache) {
        this.cache = cache;
    }

    public Boolean getLogging() {
        return logging;
    }

    public void setLogging(Boolean logging) {
        this.logging = logging;
    }

    public Boolean getCompress() {
        return compress;
    }

    public void setCompress(Boolean compress) {
        this.compress = compress;
    }

    public Boolean getKeepDuplicate() {
        return keepDuplicate;
    }

    public void setKeepDuplicate(Boolean keepDuplicate) {
        this.keepDuplicate = keepDuplicate;
    }

    public boolean isRetention() {
        return retention;
    }

    public void setRetention(boolean retention) {
        this.retention = retention;
    }

    public OracleStorageClause getStorageClause() {
        return storageClause;
    }

    public void setStorageClause(OracleStorageClause storageClause) {
        if (storageClause != null) {
            storageClause.setParent(this);
        }
        this.storageClause = storageClause;
    }

    public SQLExpr getPctversion() {
        return pctversion;
    }

    public void setPctversion(SQLExpr pctversion) {
        if (pctversion != null) {
            pctversion.setParent(this);
        }
        this.pctversion = pctversion;
    }

    public SQLName getSegementName() {
        return segementName;
    }

    public void setSegementName(SQLName segementName) {
        this.segementName = segementName;
    }
}
