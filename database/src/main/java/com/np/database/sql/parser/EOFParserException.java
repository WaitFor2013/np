
package com.np.database.sql.parser;

@SuppressWarnings("serial")
public class EOFParserException extends ParserException {

    public EOFParserException(){
        super("EOF");
    }
}
