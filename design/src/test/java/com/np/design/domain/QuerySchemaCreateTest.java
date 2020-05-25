package com.np.design.domain;

import com.np.database.orm.NpDatasource;
import com.np.database.orm.NpDbConfig;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.session.DefaultSqlSession;

public class QuerySchemaCreateTest {

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

            //session.executeDDL("drop table schema_view");
            //session.executeDDL(PostgresDDLHelper.generateCreateSQL(SchemaView.class));
            //session.executeDDL("CREATE UNIQUE INDEX  idx_unq_view_name ON public.schema_view  USING btree (view_name)");

            //session.executeDDL("drop table schema_result");
            //session.executeDDL(PostgresDDLHelper.generateCreateSQL(SchemaParam.class));
            session.executeDDL("DROP INDEX idx_unq_view_name_column_name");
            session.executeDDL("CREATE UNIQUE INDEX  idx_unq_view_table_column ON public.schema_param  USING btree (view_name,table_name,column_name)");

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
