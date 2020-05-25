package com.np.database;

import com.np.database.exception.NpDbException;

public enum NpFormatter {

    N("N"),
    YMDHMS("yyyy-MM-dd HH:mm:ss"),
    YMD("yyyy-MM-dd"),
    HMS("HH:mm:ss");

    private String str;

    NpFormatter(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }

    public static String[] getStringArray() {
        NpFormatter[] values = NpFormatter.values();
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].str;
        }
        return result;
    }

    public static NpFormatter parseString(String string) {
        NpFormatter[] values = NpFormatter.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].str.equals(string))
                return values[i];
        }
        throw new NpDbException("unknown NpFormatter " + string);
    }
}
