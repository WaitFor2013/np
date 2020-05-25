package com.np.database.orm;


import com.np.database.ColumnDefinition;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.biz.param.Direction;
import com.np.database.orm.session.DefaultSqlSession;

public class OrmQueryTest {

    public static void main(String[] args) {

        NpDbConfig dbConfig = NpDbConfig.builder()
                .dbTypeEnum(DbTypeEnum.POSTGRESQL)
                .ip("127.0.0.1")
                .port(8432)
                .dbName("postgres")
                .user("postgres")
                .password("postgres")
                .build();

        NpDatasource npDatasource = new NpDatasource(dbConfig);

        DefaultSqlSession session = (DefaultSqlSession) npDatasource.getSession(true);
        //Executor executor = npDatasource.newExecutor(true);

        try {

            BizParam bizParam = BizParam.NEW()
                    .notEquals(ColumnDefinition.name("id"),999)
                    .notLike(ColumnDefinition.name("username"),"aji")
                    .fullText("@",ColumnDefinition.name("email"),ColumnDefinition.name("username"))
                    .orderBy(ColumnDefinition.name("id"),Direction.DESC)
                    .page(0,1);

            /*List<Author> query1 = session.query("SELECT a.id,a,username,a.password,a.email,a.favourite_section FROM author a", bizParam, Author.class);
            Author author = query1.get(0);
            System.out.println("ID:"+author.getId());

            int count = session.count("SELECT a.id,a,username,a.password,a.email,a.favourite_section FROM author a", bizParam, Author.class);*/

            //System.out.println(query1.get(0).getUsername());

            //System.out.println(count);


        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 不允许 select *
         *
         */


        //系统启动预初始化阶段
        /**
         *
         * 查询SQL加载到内存中
         * 语言条件
         *
         */

        //运行阶段
        /**
         *
         * 基于入参有无，动态设置参数
         * no repeat <if test></>
         *
         * 约定NULL值处理和空值处理  在查询时 & 在设置时
         *
         */
    }

}
