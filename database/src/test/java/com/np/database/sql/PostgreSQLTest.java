package com.np.database.sql;

import com.alibaba.fastjson.JSON;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLSelectItem;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.np.database.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.np.database.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;

import java.util.List;
import java.util.Map;


public class PostgreSQLTest {

    public static void main(String[] args) {

        NpSqlHelper.FormatOption formatOption = new NpSqlHelper.FormatOption();
        formatOption.setUppCase(true);

        //System.out.println("1、SQL美化**************");
        String tSql = "select fix(a.name) as eee,a.age,b.rolename,c.emp_name from t_user a left join t_role b on a.rid = b.id left join t_emp c on a.id = c.id where a.id = ? and b.xx= ?";
        //System.out.println(NpSqlHelper.formatPGSql(tSql, formatOption));

        System.out.println("2、获取表&字段**************");
        //List<SQLStatement> sqlStatements = NpSqlHelper.parseStatements(tSql, JdbcConstants.POSTGRESQL);
        PGSQLStatementParser parser = new PGSQLStatementParser(tSql);
        SQLStatement statement = parser.parseStatement();

        PGSelectStatement selectStatement = (PGSelectStatement) statement;
        List<SQLSelectItem> selectList = selectStatement.getSelect().getFirstQueryBlock().getSelectList();

        PGSchemaStatVisitor pgSchemaStatVisitor = new PGSchemaStatVisitor();
        statement.accept(pgSchemaStatVisitor);

        
        //SQLMethodInvokeExpr
        //SQLPropertyExpr

        //2.1、获取表名称
        Map<TableStat.Name, TableStat> tables = pgSchemaStatVisitor.getTables();


        for (Map.Entry entry : tables.entrySet()) {
            System.out.println(JSON.toJSONString(entry));
        }

        //
        System.out.println(JSON.toJSONString(pgSchemaStatVisitor.getTablePrefixMap()));

        //2.2、获取字段名称
        for (TableStat.Column column : pgSchemaStatVisitor.getColumns()) {
            System.out.println(JSON.toJSONString(column));
        }

        //System.out.println(pgSchemaStatVisitor.getParameters());
        System.out.println();

    }
}
