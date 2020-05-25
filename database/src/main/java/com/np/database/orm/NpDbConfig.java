package com.np.database.orm;

import com.np.database.exception.NpDbException;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.biz.DbTypeEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NpDbConfig {

    private DbTypeEnum dbTypeEnum;
    private String ip;
    private Integer port;
    private String dbName;
    private String user;
    private String password;
    private Boolean log;

    public void validate() {
        if (null == dbTypeEnum
                || null == ip
                || null == port
                || null == dbName
                || null == user
                || null == password) {

            throw new NpDbException("dbTypeEnum or ip or port or dbName or user or password can't be null.");
        }
    }
}
