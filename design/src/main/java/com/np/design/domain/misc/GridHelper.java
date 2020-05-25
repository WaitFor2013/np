package com.np.design.domain.misc;

import com.np.database.NpDataType;
import com.np.database.NpFormatter;
import com.np.database.orm.NpOrmContext;
import com.np.database.recommend.SysColumn;
import com.np.database.recommend.SysTable;
import com.np.database.reflection.MetaObject;
import com.np.design.domain.NpModule;
import com.np.design.domain.TableDesign;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.ui.NpGridModel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GridHelper {

    public static <E, T> T copyFromTo(E from, T to) {
        MetaObject fromMeta = NpOrmContext.newMetaObject(from);
        MetaObject toMeta = NpOrmContext.newMetaObject(to);
        for (Field field : from.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            if (fromMeta.hasGetter(fieldName) && toMeta.hasSetter(fieldName))
                toMeta.setValue(fieldName, fromMeta.getValue(fieldName));
        }
        return to;
    }

    public static List<SchemaColumn> initColumns(String tableName) {
        List<SchemaColumn> schemaColumns = new ArrayList<>();
        schemaColumns.add(SchemaColumn.builder()
                .tableName(tableName).columnName(SysColumn.tenant_id.toString()).columnComment("[*]租户ID")
                .columnType(NpDataType.STR64.toString()).render(SysTable.sys_tenant.toString())
                .isRealTime(false).isAllOnly(false).isTenantOnly(true)
                .isNull(false).isAuth(false).defaultOrder(OrderOption.N.toString())
                .build());

        schemaColumns.add(SchemaColumn.builder()
                .tableName(tableName).columnName(SysColumn.sys_id.toString()).columnComment("[*]系统ID")
                .columnType(NpDataType.STR64.toString()).render(SysTable.N.toString())
                .isRealTime(false).isAllOnly(true).isTenantOnly(false)
                .isNull(false).isAuth(false).defaultOrder(OrderOption.N.toString())
                .build());

        schemaColumns.add(SchemaColumn.builder()
                .tableName(tableName).columnName(SysColumn.gmt_create.toString()).columnComment("[*]创建时间")
                .columnType(NpDataType.DATE.toString()).render(SysTable.N.toString())
                .isRealTime(false).isAllOnly(false).isTenantOnly(false)
                .isNull(false).isAuth(false).defaultOrder(OrderOption.N.toString())
                .build());

        schemaColumns.add(SchemaColumn.builder()
                .tableName(tableName).columnName(SysColumn.gmt_modified.toString()).columnComment("[*]修改时间")
                .columnType(NpDataType.DATE.toString()).render(SysTable.N.toString())
                .isRealTime(false).isAllOnly(false).isTenantOnly(false)
                .isNull(false).isAuth(false).defaultOrder(OrderOption.N.toString())
                .build());

        schemaColumns.add(SchemaColumn.builder()
                .tableName(tableName).columnName(SysColumn.create_user_id.toString()).columnComment("[*]创建用户")
                .columnType(NpDataType.STR64.toString()).render(SysTable.sys_user.toString())
                .isRealTime(false).isAllOnly(false).isTenantOnly(false)
                .isNull(false).isAuth(false).defaultOrder(OrderOption.N.toString())
                .build());

        schemaColumns.add(SchemaColumn.builder()
                .tableName(tableName).columnName(SysColumn.modify_user_id.toString()).columnComment("[*]修改用户")
                .columnType(NpDataType.STR64.toString()).render(SysTable.sys_user.toString())
                .isRealTime(false).isAllOnly(false).isTenantOnly(false)
                .isNull(false).isAuth(false).defaultOrder(OrderOption.N.toString())
                .build());

        schemaColumns.add(SchemaColumn.builder()
                .tableName(tableName).columnName(SysColumn.ext_fields.toString()).columnComment("[*]动态扩展")
                .columnType(NpDataType.JSON.toString()).render(SysTable.N.toString())
                .isRealTime(false).isAllOnly(false).isTenantOnly(false)
                .isNull(false).isAuth(false).defaultOrder(OrderOption.N.toString())
                .build());

        return schemaColumns;
    }

    public static boolean checkEditable(String columnName, Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            GridField annotation = field.getAnnotation(GridField.class);
            if (null != annotation && annotation.name().equals(columnName) && "false".equals(annotation.editable())) {
                return false;
            }
        }
        return true;
    }

    public static void initTable(JTable jTable, NpGridModel npGridModel, Class clazz) {

        //jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Field[] declaredFields = clazz.getDeclaredFields();

        int i = 0;
        TableColumnModel columnModel = jTable.getColumnModel();


        for (Field field : declaredFields) {
            GridField annotation = field.getAnnotation(GridField.class);
            if (null != annotation) {

                TableColumn tableColumn = columnModel.getColumn(i);

                //tableColumn.setCellRenderer(defaultTableCellRenderer);

                //dict init
                if (!annotation.dict().isEmpty()) {

                    if (annotation.dict().equals("module")) {
                        tableColumn.setCellEditor(new DefaultCellEditor(new JComboBox(NpModule.getStringArray())));
                    }
                    if (annotation.dict().equals("render")) {
                        tableColumn.setCellEditor(new DefaultCellEditor(new JComboBox(TableDesign.otherTables())));
                    }
                    if (annotation.dict().equals("dataType")) {
                        tableColumn.setCellEditor(new DefaultCellEditor(new JComboBox(NpDataType.getStringArray())));
                    }
                    if (annotation.dict().equals("formatter")) {
                        tableColumn.setCellEditor(new DefaultCellEditor(new JComboBox(NpFormatter.getStringArray())));
                    }
                    if (annotation.dict().equals("OrderOption")) {
                        tableColumn.setCellEditor(new DefaultCellEditor(new JComboBox(OrderOption.getStringArray())));
                    }
                }
                //maxWidth init
                if (!annotation.maxWidth().isEmpty()) {
                    int size = Integer.parseInt(annotation.maxWidth());
                    tableColumn.setMaxWidth(size);
                }

                if (!annotation.minWidth().isEmpty()) {
                    int size = Integer.parseInt(annotation.minWidth());
                    tableColumn.setMinWidth(size);
                }

                i++;
            }
        }

    }


    public static String[] getColumns(Class clazz) {

        Field[] declaredFields = clazz.getDeclaredFields();

        List<String> result = new ArrayList<>();
        for (Field field : declaredFields) {
            GridField annotation = field.getAnnotation(GridField.class);
            if (null != annotation) {
                result.add(annotation.name());
            }
        }

        return result.toArray(new String[result.size()]);
    }

    public static Object[] getInitValues(Class clazz) {

        Field[] declaredFields = clazz.getDeclaredFields();

        List<Object> result = new ArrayList<>();
        for (Field field : declaredFields) {

            GridField gridField = field.getAnnotation(GridField.class);

            if (null != gridField) {
                GridCellInit annotation = field.getAnnotation(GridCellInit.class);
                Object obj = null;
                if (null != annotation) {
                    if (!annotation.boolString().isEmpty()) {
                        obj = Boolean.valueOf(annotation.boolString());
                    } else if (!annotation.intString().isEmpty()) {
                        obj = Integer.valueOf(annotation.intString());
                    } else if (!annotation.string().isEmpty()) {
                        obj = annotation.string();
                    }
                }

                result.add(obj);
            }
        }

        return result.toArray();
    }


    public static Class[] getColumnTypes(Class clazz) {

        Field[] declaredFields = clazz.getDeclaredFields();

        List<Class> result = new ArrayList<>();
        for (Field field : declaredFields) {
            GridField gridField = field.getAnnotation(GridField.class);

            if (null != gridField) {
                result.add(field.getType());
            }
        }

        return result.toArray(new Class[result.size()]);
    }


}
