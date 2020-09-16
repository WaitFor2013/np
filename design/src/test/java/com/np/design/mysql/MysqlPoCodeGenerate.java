package com.np.design.mysql;

import com.np.database.recommend.SysColumn;
import com.np.design.exception.NpException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
public class MysqlPoCodeGenerate {

    public static final String PO = "PO";

    private static final String FRN = ";\n";
    private static final String RN = "\n";
    private static final String SPACE = " ";
    private static final String SPACE4 = "    ";


    public static void appendImport(StringBuilder stringBuilder, List<MysqlColumn> columnList) {

        boolean isDecimal = false;
        boolean isDate = false;

        for (MysqlColumn schemaColumn : columnList) {
            //NpDataType npDataType = NpDataType.parseString(schemaColumn.getColumnType());
            switch (schemaColumn.getDataType()) {
                /*case DECIMAL:
                    if (!isDecimal) {
                        stringBuilder.append("import").append(SPACE).append("java.math.BigDecimal").append(FRN);
                        isDecimal = true;
                    }
                    break;*/
                case "datetime":
                    if (!isDate) {
                        stringBuilder.append("import").append(SPACE).append("java.util.Date").append(FRN);
                        isDate = true;
                    }
                    break;

            }
        }
    }



    public static String generate(String packageStr, MysqlTable table, List<MysqlColumn> columnList) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package").append(SPACE).append(packageStr).append(FRN);
        stringBuilder.append(RN);

        stringBuilder.append("import").append(SPACE).append("com.np.database.*").append(FRN);

        stringBuilder.append("import").append(SPACE).append("lombok.*").append(FRN).append(RN);

        stringBuilder.append("import").append(SPACE).append("javax.persistence.Column").append(FRN);
        stringBuilder.append("import").append(SPACE).append("javax.persistence.Id").append(FRN);
        stringBuilder.append("import").append(SPACE).append("javax.persistence.Table").append(FRN);
        appendImport(stringBuilder, columnList);
        stringBuilder.append(RN);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");

        stringBuilder.append("//code generate ").append(simpleDateFormat.format(new Date())).append(RN);
        stringBuilder.append("//").append(table.getTableComment()).append(RN);
        stringBuilder.append("@Getter").append(RN);
        stringBuilder.append("@Setter").append(RN);
        stringBuilder.append("@Table(name = \"").append(table.getTableName()).append("\")").append(RN);


        stringBuilder.append("@Builder").append(RN);
        stringBuilder.append("@AllArgsConstructor").append(RN);
        stringBuilder.append("@NoArgsConstructor").append(RN);
        stringBuilder.append("@ToString").append(RN);


        String className = toCamel(table.getTableName(), true) + PO;
        stringBuilder.append("public class ").append(className).append(SPACE).append("implements NoRepeatPO").append(SPACE).append("{").append(RN);
        stringBuilder.append(RN);

        if (!table.getTableName().startsWith("schema")) {
            stringBuilder.append(SPACE4).append("static {").append(RN);
            stringBuilder.append(SPACE4).append(SPACE4).append("NpDefinition.registry(\"").append(table.getTableName()).append("\", ").append(className).append(".class)").append(FRN);
            stringBuilder.append(SPACE4).append("}").append(RN);
        }

        stringBuilder.append(RN);

        StringBuilder createCodeHelper = new StringBuilder();
        StringBuilder defaultCodeHelper = new StringBuilder();
        Set<String> sysColumnsValueSet = SysColumn.getValueSet();
        for (int i = 0; i < columnList.size(); i++) {
            MysqlColumn schemaColumn = columnList.get(i);
            stringBuilder.append(RN);
            stringBuilder.append(SPACE4).append("//").append(schemaColumn.getColumnComment()).append(RN);
            //public static final ColumnDefinition $tenantId = ColumnDefinition.name("tenant_id");
            stringBuilder.append(SPACE4).append("public static final ColumnDefinition $").append(toCamel(schemaColumn.getColumnName(), false))
                    .append(" = ColumnDefinition.tableAndColumn(\"").append(schemaColumn.getTableName()).append("\",\"").append(schemaColumn.getColumnName()).append("\")").append(FRN);

            if (SysColumn.sys_id.toString().equals(schemaColumn.getColumnName())) {
                stringBuilder.append(SPACE4).append("@Id").append(RN);
            }
            stringBuilder.append(SPACE4).append("@Column(name = \"").append(schemaColumn.getColumnName()).append("\")").append(RN);

            stringBuilder.append(SPACE4).append("private ").append(getFromType(schemaColumn.getDataType())).append(SPACE).append(toCamel(schemaColumn.getColumnName(), false)).append(FRN);

            if (!sysColumnsValueSet.contains(schemaColumn.getColumnName())) {
                createCodeHelper.append(".").append(toCamel(schemaColumn.getColumnName(), false)).append("()");
            } else {
                defaultCodeHelper.append(".").append(toCamel(schemaColumn.getColumnName(), false)).append("()");
            }

        }

        stringBuilder.append(RN);
        stringBuilder.append(SPACE4).append("//code helper").append(RN);
        stringBuilder.append(SPACE4).append("//").append(className).append(".builder()").append(createCodeHelper.toString()).append(FRN);
        stringBuilder.append(SPACE4).append("//").append(defaultCodeHelper.toString()).append(FRN);
        stringBuilder.append(RN);
        stringBuilder.append("}");


        return stringBuilder.toString();
    }

    public static String getFromType(String columnType) {
        switch (columnType){
            case "varchar":
                return "String";
            case "int":
            case "tinyint":
                return "Integer";
            case "bigint":
                return "Long";
            case "datetime":
            case "timestamp":
                return "Date";
        }
        throw new NpException("尚未支持的类型:"+columnType);
    }

    public static String toCamel(String str, boolean isFirstUp) {
        StringBuilder builder = new StringBuilder();
        char split = '_';
        boolean needUp = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if ((isFirstUp && 0 == i) || needUp) {
                c = Character.toUpperCase(c);
            }
            if (split == c) {
                needUp = true;
                continue;
            }

            builder.append(c);
            needUp = false;
        }
        return builder.toString();
    }
}
