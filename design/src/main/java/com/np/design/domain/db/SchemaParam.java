package com.np.design.domain.db;

import com.np.database.orm.NpOrmContext;
import com.np.database.orm.SerialId;
import com.np.database.reflection.MetaObject;
import com.np.design.domain.misc.GridCellInit;
import com.np.design.domain.misc.GridField;
import com.np.design.domain.misc.PgType;
import com.np.design.domain.misc.GridCellInit;
import com.np.design.domain.misc.GridField;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Table(name = "schema_param")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Slf4j
public class SchemaParam implements NpTypeInterface {

    @SerialId
    @Column(name = "id")
    @PgType(column = " SERIAL ")
    private Integer id;

    @Column(name = "view_name")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String viewName;

    @Column(name = "table_prefix")
    @GridField(name = "表前缀", maxWidth = "200")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String tablePrefix;

    @Column(name = "table_name")
    @GridField(name = "表名", maxWidth = "200")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String tableName;

    @Column(name = "column_name")
    @GridField(name = "列名", maxWidth = "200")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String columnName;

    @Column(name = "column_type")
    @GridField(name = "列类型", maxWidth = "250", dict = "dataType")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String columnType;

    @Column(name = "column_comment")
    @GridField(name = "列中文名", maxWidth = "200")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String columnComment;

    @Column(name = "is_param")
    @GridField(name = "参数", maxWidth = "45")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "false")
    private Boolean isParam;

    @Column(name = "is_result")
    @GridField(name = "结果集", maxWidth = "45")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "false")
    private Boolean isResult;

    public static SchemaParam copyFrom(SchemaParam temp) {
        SchemaParam result = new SchemaParam();
        result.viewName = temp.getViewName();
        result.tablePrefix = temp.getTablePrefix();
        result.tableName = temp.getTableName();
        result.columnName = temp.getColumnName();
        result.columnType = temp.getColumnType();
        result.columnComment = temp.getColumnComment();
        result.isParam = temp.getIsParam();
        result.isResult = temp.getIsResult();

        return result;
    }

    public Object[] getRow() {
        MetaObject metaObject = NpOrmContext.newMetaObject(this);
        List<Object> result = new ArrayList<>();
        for (Field field : SchemaParam.class.getDeclaredFields()) {
            if (null != field.getAnnotation(GridField.class)) {
                result.add(metaObject.getValue(field.getName()));
            }
        }
        return result.toArray();
    }

    public static SchemaParam newInstance(String[] header, Object[] data) {

        SchemaParam schemaParam = new SchemaParam();
        MetaObject metaObject = NpOrmContext.newMetaObject(schemaParam);
        Map<String, String> fieldPair = SchemaTable.getFieldPair(SchemaParam.class);
        for (int i = 0; i < data.length; i++) {
            String field = fieldPair.get(header[i]);
            metaObject.setValue(field, data[i]);
        }

        return schemaParam;
    }
}
