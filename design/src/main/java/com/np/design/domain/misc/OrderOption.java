package com.np.design.domain.misc;

public enum OrderOption {
    N, ASC, DESC;


    public static String[] getStringArray() {
        OrderOption[] values = OrderOption.values();
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].toString();
        }
        return result;
    }
}
