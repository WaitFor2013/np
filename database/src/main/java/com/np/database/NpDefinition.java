package com.np.database;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.*;

public class NpDefinition {

    public final static Map<String, Class<? extends NoRepeatPO>> cache = new HashMap<>();

    public final static Map<String, Class<? extends NoRepeatPO>> abbrCache = new HashMap<>();

    private final static Map<String, ExtTable> extTableHashMap = new HashMap<>();

    private final static Map<String, List<String>> columnsMap = new HashMap<>();

    private final static Map<String, Map<String, ExtColumn>> extTableColumnHashMap = new HashMap<>();

    private final static Map<String, String> nameColumnMap = new HashMap<>();

    private final static Map<String, String> selfColumnMap = new HashMap<>();

    private final static Set<String> treeTable = new HashSet<>();

    public static Boolean isTree(String tableName) {
        return treeTable.contains(tableName);
    }

    public static String getSelfColumn(String tableName) {
        return selfColumnMap.get(tableName);
    }


    public static void registry(String tableName, Class<? extends NoRepeatPO> clazz) {
        cache.put(tableName, clazz);

        ExtTable extTable = clazz.getAnnotation(ExtTable.class);
        extTableHashMap.put(tableName, extTable);
        abbrCache.put(extTable.abbr(), clazz);

        Field[] fields = clazz.getDeclaredFields();
        Map<String, ExtColumn> columnMap = new HashMap<>();
        List<String> columnList = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            Column columnAnnotation = field.getAnnotation(Column.class);

            if (null != columnAnnotation) {
                columnList.add(columnAnnotation.name());

                ExtColumn extColumn = field.getAnnotation(ExtColumn.class);

                if (extColumn.relation().equals("SELF")) {
                    treeTable.add(tableName);
                    selfColumnMap.put(tableName, columnAnnotation.name());
                }

                if (extColumn.isAuth()) {
                    nameColumnMap.put(tableName, columnAnnotation.name());
                }

                columnMap.put(columnAnnotation.name(), extColumn);
            }
        }

        columnsMap.put(tableName, columnList);

        extTableColumnHashMap.put(tableName, columnMap);
    }

    public static List<String> getColumnList(String tableName) {
        return columnsMap.get(tableName);
    }

    public static Class<? extends NoRepeatPO> getTableClazzByAbbr(String abbr) {
        return abbrCache.get(abbr);
    }

    public static String getNameColumn(String tableName) {
        return nameColumnMap.get(tableName);
    }

    public static Class<? extends NoRepeatPO> getTableClazz(String tableName) {
        return cache.get(tableName);
    }

    public static ExtTable getExtTable(String tableName) {
        if (extTableHashMap.containsKey(tableName)) {
            return extTableHashMap.get(tableName);
        }

        return null;
    }

    public static String getTableAbbr(String tableName) {
        if (extTableHashMap.containsKey(tableName)) {
            return extTableHashMap.get(tableName).abbr();
        }
        return null;
    }

    public static Map<String, ExtColumn> getColumns(String tableName) {
        if (extTableColumnHashMap.containsKey(tableName)) {
            return extTableColumnHashMap.get(tableName);
        }
        return null;
    }

    public static ExtColumn getExtColumn(String tableName, String columnName) {
        if (extTableColumnHashMap.containsKey(tableName)) {
            Map<String, ExtColumn> extColumnMap = extTableColumnHashMap.get(tableName);
            if (extColumnMap.containsKey(columnName)) {
                return extColumnMap.get(columnName);
            }
        }
        return null;
    }


}
