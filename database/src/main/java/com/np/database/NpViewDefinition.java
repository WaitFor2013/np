package com.np.database;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NpViewDefinition {

    public static final String WHERE_ANNOTATION = "@WHERE";

    private static final Map<String, ExtView> viewMap = new HashMap<>();

    public final static Map<String, Class<? extends NoRepeatView>> viewClassMap = new HashMap<>();

    private static final Map<String, String> noWhereSqlMap = new HashMap<>();

    private static final Map<String, List<ExtParam>> resultSetMap = new HashMap<>();

    private static final Map<String, List<ViewQueryParam>> queryParamsMap = new HashMap<>();

    public static ExtView getView(String viewName) {
        return viewMap.get(viewName);
    }

    public static Class<? extends NoRepeatView> getViewClass(String viewName) {
        return viewClassMap.get(viewName);
    }

    public static String getNoWhereSql(String viewName) {
        return noWhereSqlMap.get(viewName);
    }

    public static List<ExtParam> getResultSet(String viewName) {
        return resultSetMap.get(viewName);
    }

    public static List<ViewQueryParam> getQueryParams(String viewName) {
        return queryParamsMap.get(viewName);
    }


    public static void registrySQL(String viewName, String noWhereSql) {
        noWhereSqlMap.put(viewName, noWhereSql);
    }

    public static void registry(String viewName, Class<? extends NoRepeatView> clazz) {

        viewClassMap.put(viewName, clazz);

        ExtView extView = clazz.getAnnotation(ExtView.class);
        viewMap.put(viewName, extView);

        Field[] fields = clazz.getDeclaredFields();

        List<ExtParam> params = new ArrayList<>();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            ExtParam extParam = field.getAnnotation(ExtParam.class);
            if (null != extParam) {
                params.add(extParam);
            }
        }

        resultSetMap.put(viewName, params);
    }

    public static void registryQueryParam(String viewName, String tablePrefix, String tableName, String columnName, String columnType) {

        if (!queryParamsMap.containsKey(viewName)) {
            queryParamsMap.put(viewName, new ArrayList<>());
        }

        List<ViewQueryParam> viewQueryParams = queryParamsMap.get(viewName);

        viewQueryParams.add(new ViewQueryParam(tablePrefix, tableName, columnName, columnType));
    }

    @Getter
    @Setter
    public static class ViewQueryParam {
        private String tablePrefix;
        private String tableName;
        private String columnName;
        private String columnType;

        public ViewQueryParam(String tablePrefix, String tableName, String columnName, String columnType) {
            this.tablePrefix = tablePrefix;
            this.tableName = tableName;
            this.columnName = columnName;
            this.columnType = columnType;
        }
    }


}
