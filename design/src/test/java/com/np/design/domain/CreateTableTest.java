package com.np.design.domain;

import com.alibaba.fastjson.JSON;
import com.np.database.ColumnDefinition;
import com.np.database.orm.NpDatasource;
import com.np.database.orm.NpDbConfig;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.session.DefaultSqlSession;
import com.np.design.domain.ddl.PgTable;
import com.np.design.domain.ddl.PgTable;

import java.util.List;
import java.util.Map;

public class CreateTableTest {

    public static void main(String[] args) {

        NpDbConfig dbConfig = NpDbConfig.builder()
                .dbTypeEnum(DbTypeEnum.POSTGRESQL)
                .ip("127.0.0.1")
                .port(8432)
                .dbName("postgres")
                .user("postgres")
                .password("postgres")
                .log(true)
                .build();

        NpDatasource npDatasource = new NpDatasource(dbConfig);

        DefaultSqlSession session = (DefaultSqlSession) npDatasource.getSession(true);

        try {

            Integer query = session.count("select count(*) from schema_table", BizParam.NEW(), Map.class);
            System.out.println(JSON.toJSONString(query));

            List<PgTable> pgTables = session.queryAll(BizParam.NEW().notIn(ColumnDefinition.name("schemaname"), new Object[]{"pg_catalog", "information_schema", "sys"}), PgTable.class);

            if (null != pgTables) {
                for (PgTable tmp : pgTables) {
                    //System.out.println(tmp.getTablename());
                    if ("schema_table".equals(tmp.getTablename()) || "schema_column".equals(tmp.getTablename())) {

                    } else {
                        //session.executeDDL("drop table " + tmp.getTablename());
                    }
                }
            }
            //session.executeDDL("drop table schema_table");
            //session.executeDDL(PostgresDDLHelper.generateSQL(SchemaTable.class));
            session.executeDDL("CREATE UNIQUE INDEX  idx_unq_table_name ON public.schema_table  USING btree (table_name)");

            //session.executeDDL("drop table schema_column");
            //session.executeDDL(PostgresDDLHelper.generateSQL(SchemaColumn.class));
            session.executeDDL("CREATE UNIQUE INDEX  idx_unq_table_name_column_name ON public.schema_column  USING btree (table_name,column_name)");

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
