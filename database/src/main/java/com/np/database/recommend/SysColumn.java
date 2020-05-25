package com.np.database.recommend;

import java.util.HashSet;
import java.util.Set;

public enum SysColumn {
    tenant_id,
    sys_id,
    gmt_create,
    gmt_modified,
    create_user_id,
    modify_user_id,
    ext_fields;

    public static Set<String> getValueSet() {
        Set<String> result = new HashSet<>();
        for (SysColumn sysColumn : SysColumn.values()) {
            result.add(sysColumn.toString());
        }
        return result;
    }
}
