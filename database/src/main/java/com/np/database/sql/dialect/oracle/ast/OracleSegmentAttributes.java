
package com.np.database.sql.dialect.oracle.ast;

import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;
import com.np.database.sql.ast.SQLName;
import com.np.database.sql.ast.SQLObject;

/**
 * Created by wenshao on 21/05/2017.
 */
public interface OracleSegmentAttributes extends SQLObject {

    SQLName getTablespace();
    void setTablespace(SQLName name);

    Boolean getCompress();

    void setCompress(Boolean compress);

    Integer getCompressLevel();

    void setCompressLevel(Integer compressLevel);

    Integer getInitrans();
    void setInitrans(Integer initrans);

    Integer getMaxtrans();
    void setMaxtrans(Integer maxtrans);

    Integer getPctincrease();
    void setPctincrease(Integer pctincrease);

    Integer getPctused();
    void setPctused(Integer pctused);

    Integer getPctfree();
    void setPctfree(Integer ptcfree);

    Boolean getLogging();
    void setLogging(Boolean logging);

    SQLObject getStorage();
    void setStorage(SQLObject storage);

    boolean isCompressForOltp();

    void setCompressForOltp(boolean compressForOltp);
}
