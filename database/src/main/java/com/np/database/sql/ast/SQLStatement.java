
package com.np.database.sql.ast;

import java.util.List;

public interface SQLStatement extends SQLObject {
    String          getDbType();
    boolean         isAfterSemi();
    void            setAfterSemi(boolean afterSemi);
    SQLStatement    clone();
    List<SQLObject> getChildren();
    String          toLowerCaseString();

    List<SQLCommentHint> getHeadHintsDirect();
    void setHeadHints(List<SQLCommentHint> headHints);
}
