
package com.np.database.sql.parser;

public class NotAllowCommentException extends ParserException {

    private static final long serialVersionUID = 1L;

    public NotAllowCommentException(){
        this("comment not allow");
    }

    public NotAllowCommentException(String message, Throwable e){
        super(message, e);
    }

    public NotAllowCommentException(String message){
        super(message);
    }

}
