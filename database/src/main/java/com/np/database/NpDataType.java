package com.np.database;

import com.np.database.exception.NpDbException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public enum NpDataType {

    BOOL("布尔:BOOLEAN"),
    INT("整型:INT"),
    LONG("长整型:LONG"),
    DECIMAL("浮点:DECIMAL"),
    STR64("字符串:STR(64)"),
    STR256("字符串:STR(256)"),
    STR1024("字符串:STR(1024)"),
    TEXT("文本:TEXT"),
    DATE("时间:DATE"),
    ARRAY_STR("字符串数组:ARRAY_STR"),
    ARRAY_INT("整型数组:ARRAY_INT"),
    JSON("JSON");

    private String str;

    NpDataType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public static String[] getStringArray() {
        NpDataType[] values = NpDataType.values();
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].str;
        }
        return result;
    }

    public static NpDataType parseString(String string) {
        NpDataType[] values = NpDataType.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].str.equals(string))
                return values[i];
        }
        throw new NpDbException("unknown NpDataType " + string);
    }

    public static Class getFromTypeClazz(String columnType) {

        NpDataType npDataType = NpDataType.parseString(columnType);
        switch (npDataType) {
            case INT:
                return Integer.class;
            case LONG:
                return Long.class;
            case DECIMAL:
                return BigDecimal.class;
            case STR64:
                return String.class;
            case STR256:
                return String.class;
            case STR1024:
                return String.class;
            case TEXT:
                return String.class;
            case BOOL:
                return Boolean.class;
            case DATE:
                return Date.class;
            case ARRAY_INT:
                return List.class;
            case ARRAY_STR:
                return List.class;
            case JSON:
                return String.class;
        }
        throw new NpDbException("unsupported column type.");
    }

    public static String getFromType(String columnType) {
        NpDataType npDataType = NpDataType.parseString(columnType);
        switch (npDataType) {
            case INT:
                return "Integer";
            case LONG:
                return "Long";
            case DECIMAL:
                return "BigDecimal";
            case STR64:
                return "String";
            case STR256:
                return "String";
            case STR1024:
                return "String";
            case TEXT:
                return "String";
            case BOOL:
                return "Boolean";
            case DATE:
                return "Date";
            case ARRAY_INT:
                return "List<Integer>";
            case ARRAY_STR:
                return "List<String>";
            case JSON:
                return "String";
        }
        throw new NpDbException("unsupported column type.");
    }

}
