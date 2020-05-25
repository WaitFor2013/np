package com.np.design.domain.code.generate;

import com.np.database.NpDataType;
import com.np.database.NpViewDefinition;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.np.database.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.np.design.domain.db.SchemaParam;
import com.np.design.domain.db.SchemaView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.np.design.domain.code.generate.PoCodeGenerate.appendImport;
import static com.np.design.domain.code.generate.PoCodeGenerate.toCamel;

public class ViewCodeGenerate {

    private static final String FRN = ";\n";
    private static final String RN = "\n";
    private static final String SPACE = " ";
    private static final String SPACE4 = "    ";

    private static void init(SchemaView schemaView, String sql, List<SchemaParam> params) {

        //no where render
        PGSQLStatementParser parser = new PGSQLStatementParser(sql);
        SQLStatement statement = parser.parseStatement();
        PGSelectStatement selectStatement = (PGSelectStatement) statement;
        SQLSelectQueryBlock queryBlock = selectStatement.getSelect().getFirstQueryBlock();
        if (null != queryBlock.getWhere()) {
            String whereCause = queryBlock.getWhere().toString();
            sql = sql.replace(whereCause.toLowerCase(), "");
            sql = sql.replace("where", NpViewDefinition.WHERE_ANNOTATION);
        }

        sql = sql.replaceAll("\n", "\t");

        schemaView.setViewSql(sql);
        for (SchemaParam schemaParam : params) {
            if (null == schemaParam.getTablePrefix()) {
                schemaParam.setTablePrefix("");
            }
            if (null == schemaParam.getTableName()) {
                schemaParam.setTableName("");
            }
            if (null == schemaParam.getIsResult()) {
                schemaParam.setIsResult(false);
            }
            if (null == schemaParam.getIsParam()) {
                schemaParam.setIsParam(false);
            }
        }
    }

    public static String generate(String packageStr, SchemaView schemaView, String sql, List<SchemaParam> params) {

        init(schemaView, sql, params);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package").append(SPACE).append(packageStr).append(FRN);
        stringBuilder.append(RN);

        stringBuilder.append("import").append(SPACE).append("com.np.database.*").append(FRN);

        stringBuilder.append("import").append(SPACE).append("lombok.*").append(FRN).append(RN);

        stringBuilder.append("import").append(SPACE).append("javax.persistence.Column").append(FRN);
        stringBuilder.append("import").append(SPACE).append("javax.persistence.Id").append(FRN);
        stringBuilder.append("import").append(SPACE).append("javax.persistence.Table").append(FRN);
        appendImport(stringBuilder, params);
        stringBuilder.append(RN);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

        stringBuilder.append("//code generate ").append(simpleDateFormat.format(new Date())).append(RN);
        stringBuilder.append("//").append(schemaView.getViewComment()).append(RN);
        stringBuilder.append("@Getter").append(RN);
        stringBuilder.append("@Setter").append(RN);

        stringBuilder.append("@Builder").append(RN);
        stringBuilder.append("@AllArgsConstructor").append(RN);
        stringBuilder.append("@NoArgsConstructor").append(RN);
        stringBuilder.append("@ToString").append(RN);
        stringBuilder.append("@ExtView(")
                .append("viewName = \"").append(schemaView.getViewName()).append("\",")
                .append("viewComment = \"").append(schemaView.getViewComment()).append("\")")
                .append(RN);

        String className = toCamel(schemaView.getViewName(), true);
        stringBuilder.append("public class ").append(className).append(SPACE).append("implements NoRepeatView").append(SPACE).append("{").append(RN);
        stringBuilder.append(RN);
        stringBuilder.append(SPACE4).append("static {").append(RN);

        stringBuilder.append(SPACE4).append(SPACE4).append("NpViewDefinition.registry(\"").append(schemaView.getViewName()).append("\", ").append(className).append(".class)").append(FRN);
        stringBuilder.append(SPACE4).append(SPACE4).append("NpViewDefinition.registrySQL(\"").append(schemaView.getViewName()).append("\", \"").append(schemaView.getViewSql()).append("\")").append(FRN);
        stringBuilder.append(RN);

        for (int i = 0; i < params.size(); i++) {
            SchemaParam schemaParam = params.get(i);
            if (schemaParam.getIsParam()) {

                stringBuilder.append(SPACE4).append(SPACE4).append("NpViewDefinition.registryQueryParam(\"")
                        .append(schemaView.getViewName()).append("\", \"")
                        .append(schemaParam.getTablePrefix()).append("\", \"")
                        .append(schemaParam.getTableName()).append("\", \"")
                        .append(schemaParam.getColumnName()).append("\", \"")
                        .append(schemaParam.getColumnType()).append("\")")
                        .append(FRN);
            }
        }

        stringBuilder.append(SPACE4).append("}").append(RN);

        stringBuilder.append(RN);
        for (int i = 0; i < params.size(); i++) {
            SchemaParam schemaParam = params.get(i);
            if (schemaParam.getIsParam()) {
                String propertyName = schemaParam.getTableName() + "$" + schemaParam.getColumnName();
                //propertyName = NpOrmContext.toCamel(propertyName, false);

                stringBuilder.append(SPACE4).append("public static final ColumnDefinition ")
                        .append(propertyName).append(SPACE).append("=")
                        .append("ColumnDefinition.name(\"")
                        .append(propertyName)
                        .append("\")").append(FRN);
            }
        }

        for (int i = 0; i < params.size(); i++) {
            SchemaParam schemaParam = params.get(i);

            if (schemaParam.getIsResult()) {
                stringBuilder.append(RN);
                stringBuilder.append(SPACE4).append("//").append(schemaParam.getColumnComment()).append(RN);
                stringBuilder.append(SPACE4).append("@Column(name = \"").append(schemaParam.getColumnName()).append("\")").append(RN);
                stringBuilder.append(SPACE4).append(extColumn(schemaParam)).append(RN);
                stringBuilder.append(SPACE4).append("private ").append(NpDataType.getFromType(schemaParam.getColumnType())).append(SPACE).append(toCamel(schemaParam.getColumnName(), false)).append(FRN);
            }
        }

        stringBuilder.append(RN);
        stringBuilder.append(RN);
        stringBuilder.append("}");


        return stringBuilder.toString();
    }

    private static String extColumn(SchemaParam schemaParam) {
        StringBuilder builder = new StringBuilder("@ExtParam(");
        builder.append("tablePrefix = \"").append(schemaParam.getTablePrefix()).append("\"");
        builder.append(",tableName = \"").append(schemaParam.getTableName()).append("\"");
        builder.append(",columnName = \"").append(schemaParam.getColumnName()).append("\"");
        builder.append(",columnType = \"").append(schemaParam.getColumnType()).append("\"");
        builder.append(",columnComment = \"").append(schemaParam.getColumnComment()).append("\"");

        if (schemaParam.getIsParam()) {
            builder.append(",isParam = true");
        }
        if (!schemaParam.getIsResult()) {
            builder.append(",isResult = false");
        }

        builder.append(")");
        return builder.toString();
    }
}
