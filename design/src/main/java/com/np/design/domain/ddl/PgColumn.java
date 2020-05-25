package com.np.design.domain.ddl;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Table;

@Getter
@Setter
@Table(name = "information_schema.columns")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PgColumn {

    @Column(name = "table_schema")
    private String tableSchema;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "column_name")
    private String columnName;

    @Column(name = "data_type")
    private String dataType;

    //非空
    @Column(name = "is_nullable")
    private String isNullable;

    //校验varchar
    @Column(name = "character_maximum_length")
    private Integer characterMaximumLength;

    //数组校验 _int4 && _text
    @Column(name = "udt_name")
    private String udtName;

    //浮点校验
    //numeric_precision 19
    @Column(name = "numeric_precision")
    private Integer numericPrecision;

    //numeric_scale 4
    @Column(name = "numeric_scale")
    private Integer numericScale;

}

