package com.np.database.orm;


import com.np.database.domain.Author;
import com.np.database.domain.Section;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.session.DefaultSqlSession;

public class OrmDeleteTest {

    public static void main(String[] args) {

        //1、发起执行
        NpDbConfig dbConfig = NpDbConfig.builder()
                .dbTypeEnum(DbTypeEnum.POSTGRESQL)
                .ip("127.0.0.1")
                .port(8432)
                .dbName("postgres")
                .user("postgres")
                .password("postgres")
                .build();

        NpDatasource npDatasource = new NpDatasource(dbConfig);

        DefaultSqlSession session = (DefaultSqlSession) npDatasource.getSession(false);

        try {


            Author build = Author.builder()
                    .id(202005)
                    .username("xiaoLiu")
                    .password("xxxxx")
                    .email("xiao@em.com")
                    .bio("")
                    .favouriteSection(Section.NEWS)
                    .build();

            //session.deleteById(build);

            //session.deleteByColumns(build, ColumnDefinition.name("username"));

            session.commit();

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
