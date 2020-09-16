package com.np.design.mysql;

import com.np.database.ColumnDefinition;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "information_schema.COLUMNS")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MysqlColumn {

    public static final ColumnDefinition $tableSchema = ColumnDefinition.name("TABLE_SCHEMA");
    @Column(name = "TABLE_SCHEMA")
    private String tableSchema;

    public static final ColumnDefinition $tableName = ColumnDefinition.name("TABLE_NAME");
    @Column(name = "TABLE_NAME")
    private String tableName;

    @Column(name = "COLUMN_NAME")
    private String columnName;

    @Column(name = "DATA_TYPE")
    private String dataType;

    @Column(name = "COLUMN_COMMENT")
    private String columnComment;
}
