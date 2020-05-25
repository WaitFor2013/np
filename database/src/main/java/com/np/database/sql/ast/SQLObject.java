
package com.np.database.sql.ast;

import java.util.List;
import java.util.Map;

import com.np.database.sql.visitor.SQLASTVisitor;

public interface SQLObject {
    void                accept(SQLASTVisitor visitor);
    SQLObject           clone();

    SQLObject           getParent();
    void                setParent(SQLObject parent);

    Map<String, Object> getAttributes();
    boolean             containsAttribute(String name);
    Object              getAttribute(String name);
    void                putAttribute(String name, Object value);
    Map<String, Object> getAttributesDirect();
    void                output(StringBuffer buf);

    void                addBeforeComment(String comment);
    void                addBeforeComment(List<String> comments);
    List<String>        getBeforeCommentsDirect();
    void                addAfterComment(String comment);
    void                addAfterComment(List<String> comments);
    List<String>        getAfterCommentsDirect();
    boolean             hasBeforeComment();
    boolean             hasAfterComment();
}
