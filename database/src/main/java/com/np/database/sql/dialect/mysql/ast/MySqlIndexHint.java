
package com.np.database.sql.dialect.mysql.ast;

public interface MySqlIndexHint extends MySqlHint {
    public static enum Option {
        JOIN("JOIN"),
        ORDER_BY("ORDER BY"),
        GROUP_BY("GROUP BY")
        ;
        
        public final String name;
        public final String name_lcase;
        
        Option(String name) {
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
    }
}
