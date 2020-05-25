package com.np.database;

import java.util.Objects;

public class TableDefinition {

    private String tableName;

    private String tableAbbr;

    private TableDefinition(String tableName, String tableAbbr) {
        this.tableName = tableName;
        this.tableAbbr = tableAbbr;
    }

    public static TableDefinition table(String tableName, String tableAbbr) {
        return new TableDefinition(tableName, tableAbbr);
    }

    public String getTableName() {
        return tableName;
    }

    public String getTableAbbr() {
        return tableAbbr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableDefinition that = (TableDefinition) o;
        return Objects.equals(tableName, that.tableName) &&
                Objects.equals(tableAbbr, that.tableAbbr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName, tableAbbr);
    }
}
