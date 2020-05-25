
package com.np.database.sql.ast.statement;

public enum SQLObjectType {
    TABLE("TABLE"), // 
    FUNCTION("FUNCTION"), // 
    PROCEDURE("PROCEDURE"), // 
    USER("USER"), //
    DATABASE("DATABASE"), //
    ROLE("ROLE"), // 
    PROJECT("PROJECT"), // 
    PACKAGE("PACKAGE"), // 
    RESOURCE("RESOURCE"), // 
    INSTANCE("INSTANCE"), // 
    JOB("JOB"), // 
    VOLUME("VOLUME"), // 
    OfflineModel("OFFLINEMODEL"), // 
    XFLOW("XFLOW") // for odps
    ;
    
    public final String name;
    public final String name_lcase;
    
    SQLObjectType(String name) {
        this.name = name;
        this.name_lcase = name.toLowerCase();
    }
}
