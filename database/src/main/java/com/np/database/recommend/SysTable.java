package com.np.database.recommend;

import com.np.database.exception.NpDbException;

public enum SysTable {
    N,
    sys_tenant,
    sys_user;

    public static String[] getStringArray() {
        SysTable[] values = SysTable.values();
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].toString();
        }
        return result;
    }

    public static SysTable parseString(String string) {
        SysTable[] values = SysTable.values();
        for (int i = 0; i < values.length; i++) {
            if (values[i].toString().equals(string))
                return values[i];
        }
        throw new NpDbException("unknown SysTable " + string);
    }
}
