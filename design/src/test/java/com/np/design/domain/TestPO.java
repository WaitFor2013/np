package com.np.design.domain;

import com.np.database.NoRepeatPO;
import com.np.database.orm.NpDatasource;
import com.np.database.orm.NpDbConfig;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.session.DefaultSqlSession;

import java.util.Arrays;
import java.util.List;

public class TestPO {

    public static void main(String[] args) {

        NpDbConfig dbConfig = NpDbConfig.builder()
                .dbTypeEnum(DbTypeEnum.POSTGRESQL)
                .ip("10.51.14.12")
                .port(8432)
                .dbName("postgres")
                .user("postgres")
                .password("postgres")
                .log(true)
                .build();

        NpDatasource npDatasource = new NpDatasource(dbConfig);

        DefaultSqlSession session = (DefaultSqlSession) npDatasource.getSession(true);

        try {


            List<SysTenantPO> columns =
                    session.queryAll(
                            BizParam.NEW()
                                    //.between(SysTenantPO.$gmtCreate, new Date(), new Date())
                                    .arrayContains(SysTenantPO.$bussinessType, Arrays.asList(1, 2))
                            , SysTenantPO.class);

            for (NoRepeatPO schemaColumn : columns) {
                System.out.println(schemaColumn.getSysId());
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
