package com.np.database;

import java.util.Objects;

public class ColumnDefinition {

    private String table;

    private final String columnName;

    private ColumnDefinition(String columnName) {

        this.columnName = columnName;
    }

    private ColumnDefinition(String table, String columnName) {
        this.table = table;
        this.columnName = columnName;
    }

    public static ColumnDefinition name(String columnName) {
        return new ColumnDefinition(columnName);
    }

    public static ColumnDefinition tableAndColumn(String table, String columnName) {
        return new ColumnDefinition(table, columnName);
    }

    public String getColumnName() {
        return columnName;
    }

    public String getTable() {
        return table;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnDefinition that = (ColumnDefinition) o;
        return Objects.equals(table, that.table) &&
                Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(table, columnName);
    }
}
