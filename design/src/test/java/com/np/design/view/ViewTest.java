package com.np.design.view;

import com.alibaba.fastjson.JSON;
import com.np.database.ColumnDefinition;
import com.np.database.orm.NpDatasource;
import com.np.database.orm.NpDbConfig;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.session.DefaultSqlSession;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ViewTest {

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
            ColumnDefinition wk_process$flow_engine = ViewEqProcessCount.wk_process$flow_engine;

            int count = session.countView(BizParam.NEW()
                    .equals(ViewEqProcessCount.wk_process$tenant_id, "ZZZZZN"), ViewEqProcessCount.class);

            log.warn("总数{}", count);

            List<ViewEqProcessCount> viewEqProcessCounts = session.queryView(BizParam.NEW()
                    .equals(ViewEqProcessCount.wk_process$tenant_id, "ZZZZZN"), ViewEqProcessCount.class);

            for (int i = 0; i < viewEqProcessCounts.size(); i++) {
                ViewEqProcessCount viewEqProcessCount = viewEqProcessCounts.get(i);

                log.info(JSON.toJSONString(viewEqProcessCount));
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
