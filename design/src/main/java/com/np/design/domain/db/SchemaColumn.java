package com.np.design.domain.db;

import com.np.database.NpDataType;
import com.np.database.orm.NpOrmContext;
import com.np.database.orm.SerialId;
import com.np.database.recommend.SysColumn;
import com.np.database.recommend.SysTable;
import com.np.database.reflection.MetaObject;
import com.np.database.sql.util.StringUtils;
import com.np.design.domain.misc.GridCellInit;
import com.np.design.domain.misc.GridField;
import com.np.design.domain.misc.PgType;
import com.np.design.exception.NpException;
import com.np.design.domain.misc.GridCellInit;
import com.np.design.domain.misc.GridField;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;

@Getter
@Setter
@Table(name = "schema_column")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Slf4j
public class SchemaColumn implements NpTypeInterface{

    @SerialId
    @Column(name = "id")
    @PgType(column = " SERIAL ")
    private Integer id;

    public static final String C_TABLE_NAME = "table_name";
    @Column(name = C_TABLE_NAME)
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String tableName;

    public static final String C_COLUMN_NAME = "column_name";
    private static final String _CHECK_COLUMN_LABEL = "列名";
    @Column(name = C_COLUMN_NAME)
    @GridField(name = _CHECK_COLUMN_LABEL, maxWidth = "200")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String columnName;

    @Column(name = "column_comment")
    @GridField(name = "中文名", maxWidth = "150")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String columnComment;

    @Column(name = "column_type")
    @GridField(name = "类型", maxWidth = "250", dict = "dataType")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String columnType;

    @Column(name = "render")
    @GridField(name = "关联表", maxWidth = "200", dict = "render")
    @PgType(column = "VARCHAR(255) NOT NULL")
    @GridCellInit(string = "N")
    private String render;

    @Column(name = "is_tenant_only")
    @GridField(name = "联合唯一", maxWidth = "75")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "false")
    private Boolean isTenantOnly;

    @Column(name = "is_all_only")
    @GridField(name = "全局唯一", maxWidth = "75")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "false")
    private Boolean isAllOnly;

    @Column(name = "is_null")
    @GridField(name = "可空", maxWidth = "50")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "true")
    private Boolean isNull;

    @Column(name = "is_real_time")
    @GridField(name = "时序", maxWidth = "50")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "false")
    private Boolean isRealTime;

    @Column(name = "is_auth")
    @GridField(name = "名称", maxWidth = "45")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "false")
    private Boolean isAuth;

    @Column(name = "default_order")
    @GridField(name = "默认", maxWidth = "60", dict = "OrderOption")
    @PgType(column = "VARCHAR(32) NOT NULL")
    @GridCellInit(string = "N")
    private String defaultOrder;

    public static SchemaColumn newInstance(String[] header, Object[] data) {

        SchemaColumn schemaColumn = new SchemaColumn();
        MetaObject metaObject = NpOrmContext.newMetaObject(schemaColumn);
        Map<String, String> fieldPair = SchemaTable.getFieldPair(SchemaColumn.class);
        for (int i = 0; i < data.length; i++) {
            String field = fieldPair.get(header[i]);
            metaObject.setValue(field, data[i]);
        }

        return schemaColumn;
    }

    public Object[] getRow() {
        MetaObject metaObject = NpOrmContext.newMetaObject(this);
        List<Object> result = new ArrayList<>();
        for (Field field : SchemaColumn.class.getDeclaredFields()) {
            if (null != field.getAnnotation(GridField.class)) {
                result.add(metaObject.getValue(field.getName()));
            }
        }
        return result.toArray();
    }

    public static boolean isRowCanEdit(Object[] rowData) {
        int i = 0;
        for (Field field : SchemaColumn.class.getDeclaredFields()) {
            GridField annotation = field.getAnnotation(GridField.class);
            if (null != annotation) {

                if (annotation.name().equals(_CHECK_COLUMN_LABEL)) {
                    Object column = rowData[i];
                    //sysColumn can't be edit
                    if (null != column && SysColumn.getValueSet().contains(column.toString())) {
                        return false;
                    }
                }
                i++;
            }
        }
        return true;
    }

    public static void checkRow(Map<String, Object> rowData, Object[] rowArray, String[] header, String column) {

        Object colValue = rowData.get(column);
        if (null == colValue || StringUtils.isEmpty(colValue.toString())) {
            return;
        }

        if (column.equals(_CHECK_COLUMN_LABEL)) {
            SchemaTable.nameCheck(_CHECK_COLUMN_LABEL, colValue.toString());

            if (SysColumn.getValueSet().contains(colValue.toString())) {
                throw new NpException(colValue.toString() + "：列名不允许和系统默认列名重复！");
            }
        }
    }

    public static void valid(SchemaColumn schemaColumn, boolean isThrow) {


        if (StringUtils.isEmpty(schemaColumn.getColumnName())) {
            throw new NpException("列名不允许为空");
        }
        if (StringUtils.isEmpty(schemaColumn.getColumnComment())) {
            throw new NpException("中文名不允许为空;");
        }
        if (StringUtils.isEmpty(schemaColumn.getColumnType())) {
            throw new NpException("类型不允许为空;");
        }

        StringBuilder validMessage = new StringBuilder();

        if (!SysTable.N.toString().equals(schemaColumn.getRender())) {
            if (!(NpDataType.STR64.toString().equals(schemaColumn.getColumnType()) || NpDataType.ARRAY_STR.toString().equals(schemaColumn.getColumnType()))
                    ) {
                validMessage.append("有关联表，字段类型必须为：" + NpDataType.STR64 + " 或 " + NpDataType.ARRAY_STR + ";\n");
            }
            if (NpDataType.STR64.toString().equals(schemaColumn.getColumnType()) && !schemaColumn.getColumnName().endsWith("id")) {
                validMessage.append("有关联表，列名必须以id结尾;\n");
            }
            if (NpDataType.ARRAY_STR.toString().equals(schemaColumn.getColumnType()) && !schemaColumn.getColumnName().endsWith("ids")) {
                validMessage.append("有关联表Array类型，列名必须以ids结尾;\n");
            }

            if (schemaColumn.getIsRealTime()) {
                validMessage.append("有关联表，不支持时序;\n");
            }

            if (schemaColumn.getIsAllOnly()) {
                validMessage.append("关联字段，不可以全局唯一;\n");
            }
        }

        if (schemaColumn.getIsRealTime()) {
            if (schemaColumn.getIsAllOnly() || schemaColumn.getIsTenantOnly()) {
                validMessage.append("时序，不支持全局唯一或联合唯一;\n");
            }
            if (!"N".equals(schemaColumn.getDefaultOrder())) {
                validMessage.append("时序，不支持默认排序;\n");
            }
        }

        if (schemaColumn.getIsTenantOnly() && schemaColumn.getIsAllOnly()) {
            validMessage.append("联合唯一&全局唯一不可同时存在;\n");
        }

        if (schemaColumn.getIsNull() && (schemaColumn.getIsTenantOnly() || schemaColumn.getIsAllOnly())) {
            validMessage.append("唯一，字段必须非空;\n");
        }

        String validStr = validMessage.toString();
        if (!validStr.isEmpty()) {

            log.error("表：{}信息错误{}.", schemaColumn.getTableName(), validStr);

            if (isThrow) {
                throw new NpException("列[" + schemaColumn.getColumnName() + "]:\n" + validStr);
            }
        }
    }

    public static Map<String, SchemaColumn> listToMap(List<SchemaColumn> dbColumns) {
        Map<String, SchemaColumn> result = new HashMap<>();
        for (SchemaColumn column : dbColumns) {
            result.put(column.getColumnName(), column);
        }
        return result;
    }

    public static Set<String> getColumns() {
        Set<String> result = new HashSet<>();
        result.add(C_TABLE_NAME);
        result.add(C_COLUMN_NAME);
        return result;
    }

}
