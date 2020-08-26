package com.np.database.orm.biz;


public enum DbTypeEnum {
    ORACLE, MYSQL, POSTGRESQL;

    public static DbTypeEnum getFromString(String dbType) {
        for (DbTypeEnum typeEnum : DbTypeEnum.values()) {
            if (typeEnum.name().equals(dbType)) {
                return typeEnum;
            }
        }
        return null;
    }
}
