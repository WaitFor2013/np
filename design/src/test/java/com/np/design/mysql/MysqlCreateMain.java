package com.np.design.mysql;


import com.np.database.orm.NpDatasource;
import com.np.database.orm.NpDbConfig;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.session.SqlSession;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MysqlCreateMain {


    public static void main(String[] args) throws IOException {
        NpDbConfig dbConfig = NpDbConfig.builder()
                .dbTypeEnum(DbTypeEnum.MYSQL).ip("115.159.124.39").port(3306)
                .dbName("auth").user("root").password("")
                .log(false)
                .build();

        NpDatasource npDatasource = new NpDatasource(dbConfig);

        SqlSession session = npDatasource.getSession(true);

        List<MysqlTable> tables = session.queryAll(BizParam.NEW().equals(MysqlTable.$tableSchema, "auth"), MysqlTable.class);

        for (MysqlTable table : tables) {

            List<MysqlColumn> columns = session.queryAll(BizParam.NEW().equals(MysqlColumn.$tableSchema, "auth")
                            .equals(MysqlColumn.$tableName, table.getTableName()),
                    MysqlColumn.class);

            FileUtils.writeStringToFile(new File("build/" + MysqlPoCodeGenerate.toCamel(table.getTableName(), true) + "PO.java"),
                    MysqlPoCodeGenerate.generate("test", table, columns));

        }


    }

}
