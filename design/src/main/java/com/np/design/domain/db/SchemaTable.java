package com.np.design.domain.db;

import com.np.database.orm.NpOrmContext;
import com.np.database.orm.SerialId;
import com.np.database.reflection.MetaObject;
import com.np.database.sql.util.StringUtils;
import com.np.design.domain.NpModule;
import com.np.design.domain.TableDesign;
import com.np.design.domain.misc.GridCellInit;
import com.np.design.domain.misc.GridField;
import com.np.design.domain.misc.PgType;
import com.np.design.exception.NpException;
import com.np.design.domain.misc.GridCellInit;
import com.np.design.domain.misc.GridField;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Getter
@Setter
@Table(name = "schema_table")
public class SchemaTable {

    public static final String _TABLE_NAME = "表名";
    private static final String _TABLE_COMMENT = "中文名";
    private static final String _TABLE_ABBR = "缩写";
    private static final String _TABLE_MODULE = "模块";

    @SerialId
    @Column(name = "id")
    @PgType(column = " SERIAL ")
    private Integer id;

    @Column(name = "table_module")
    @GridField(name = _TABLE_MODULE, maxWidth = "160", dict = "module")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String tableModule;

    @Id
    @Column(name = "table_name")
    @GridField(name = _TABLE_NAME, maxWidth = "250")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String tableName;

    @Column(name = "table_comment")
    @GridField(name = _TABLE_COMMENT, maxWidth = "250")
    @PgType(column = "VARCHAR(255) NOT NULL")
    private String tableComment;

    @Column(name = "table_abbr")
    @GridField(name = _TABLE_ABBR, maxWidth = "80")
    @PgType(column = "VARCHAR(32) NOT NULL")
    private String tableAbbr;

    @Column(name = "export_num")
    @GridField(name = "M导出", maxWidth = "60")
    @PgType(column = "INT NOT NULL")
    @GridCellInit(intString = "2000")
    private Integer exportNum;

    @Column(name = "is_traceable")
    @GridField(name = "审CUD", maxWidth = "60")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "false")
    private Boolean isTraceable;

    @Column(name = "is_query_traceable")
    @GridField(name = "审R", maxWidth = "45")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "false")
    private Boolean isQueryTraceable;

    @Column(name = "is_auth")
    @GridField(name = "鉴权", maxWidth = "45")
    @PgType(column = "BOOLEAN NOT NULL")
    @GridCellInit(boolString = "false")
    private Boolean isAuth;

    @GridField(name = "表(一致)", maxWidth = "70", editable = "false")
    @GridCellInit(boolString = "false")
    private Boolean isRelease;

    public Object[] getRow() {
        MetaObject metaObject = NpOrmContext.newMetaObject(this);
        List<Object> result = new ArrayList<>();
        for (Field field : SchemaTable.class.getDeclaredFields()) {
            if (null != field.getAnnotation(GridField.class)) {
                result.add(metaObject.getValue(field.getName()));
            }
        }
        return result.toArray();
    }

    public static SchemaTable newInstance(String[] header, Object[] data) {

        SchemaTable schemaTable = new SchemaTable();
        MetaObject metaObject = NpOrmContext.newMetaObject(schemaTable);
        Map<String, String> fieldPair = getFieldPair(SchemaTable.class);
        for (int i = 0; i < data.length; i++) {
            String field = fieldPair.get(header[i]);
            metaObject.setValue(field, data[i]);
        }

        return schemaTable;
    }

    public static void checkRow(Map<String, Object> rowData, String column, Object oraginalValue) {
        Object colValue = rowData.get(column);
        if (null == colValue || StringUtils.isEmpty(colValue.toString())) {
            return;
        }

        if (column.equals(_TABLE_NAME)) {
            Object module = rowData.get(_TABLE_MODULE);
            if (null == module || StringUtils.isEmpty(module.toString())) {
                throw new NpException("表设计，先配置所属【" + _TABLE_MODULE + "】");
            }
            nameCheck(_TABLE_NAME, colValue.toString());
            if (null != oraginalValue && TableDesign.hasTableInDb(oraginalValue.toString())) {
                log.info("表已存在于数据库");
                throw new NpException("表已存在于数据库，不允许变更表名，只可删除重新建立。");
            }

            if (!rowData.containsKey("id") && TableDesign.hasTableInDb(colValue.toString())) {
                throw new NpException("表已存在于数据库，不允许建立相同的表名");
            }

            if(TableDesign.hasTableInCache(colValue.toString())){
                throw new NpException("表名重复！！！");
            }

            String modulePrefix = NpModule.getModulePrefix(module.toString());
            if (!colValue.toString().startsWith(modulePrefix + "_")) {
                throw new NpException(_TABLE_NAME + "必须以模块缩写开头，并以下划线(_)连接:" + modulePrefix);
            }
            if (null != oraginalValue && !oraginalValue.toString().isEmpty()) {
                //name change
                TableDesign.tableNameChange(oraginalValue.toString(), colValue.toString());
            }
        }

        if (column.equals(_TABLE_ABBR)) {
            abbreviationCheck(colValue.toString());
        }
    }

    public static void validateRow(SchemaTable schemaTable) {

        if (null == schemaTable.getTableName() || schemaTable.getTableName().isEmpty()) {
            throw new NpException(_TABLE_NAME + "不允许为空");
        }
        if (null == schemaTable.getTableModule() || schemaTable.getTableModule().isEmpty()) {
            throw new NpException(_TABLE_MODULE + "不允许为空");
        }
        if (null == schemaTable.getTableComment() || schemaTable.getTableComment().isEmpty()) {
            throw new NpException(_TABLE_COMMENT + "不允许为空");
        }
        if (null == schemaTable.getTableAbbr() || schemaTable.getTableAbbr().isEmpty()) {
            throw new NpException(_TABLE_ABBR + "不允许为空");
        }
        String modulePrefix = NpModule.getModulePrefix(schemaTable.getTableModule());
        if (!schemaTable.getTableName().startsWith(modulePrefix + "_")) {
            throw new NpException(_TABLE_NAME + "必须以模块缩写开头，并以下划线(_)连接:" + modulePrefix);
        }
    }

    public static Map<String, String> getFieldPair(Class clazz) {
        Map<String, String> map = new HashMap();
        for (Field field : clazz.getDeclaredFields()) {
            GridField annotation = field.getAnnotation(GridField.class);
            if (null != annotation) {
                map.put(annotation.name(), field.getName());
            }
        }
        return map;
    }

    public static void abbreviationCheck(String value) {
        String REGEX_NAME = "([A-Z])*";

        if (value.length() != 3 || !Pattern.matches(REGEX_NAME, value)) {
            throw new NpException("[" + _TABLE_ABBR + "]:" + value + ",格式错误:只能由三个大写字母组成。");
        }
    }

    public static void nameCheck(String label, String name) {
        String REGEX_NAME = "([a-z]|_)*";

        if (!Pattern.matches(REGEX_NAME, name)) {
            throw new NpException("[" + label + "]:" + name + ",格式错误:只能由小写字母和下划线组成。");
        }
    }

    public static SchemaTable contains(List<SchemaTable> list, String tableName) {
        if (null != list) {
            for (SchemaTable tmp : list) {
                if (tmp.getTableName().equals(tableName))
                    return tmp;
            }
        }
        return null;
    }
}
