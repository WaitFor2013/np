package com.np.design.domain.code.generate;

import com.np.database.NpDataType;
import com.np.database.recommend.SysColumn;
import com.np.design.domain.db.NpTypeInterface;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.domain.db.SchemaTable;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
public class PoCodeGenerate {

    public static final String PO = "PO";

    private static final String FRN = ";\n";
    private static final String RN = "\n";
    private static final String SPACE = " ";
    private static final String SPACE4 = "    ";


    public static void appendImport(StringBuilder stringBuilder, List<? extends NpTypeInterface> columnList) {

        boolean isDecimal = false;
        boolean isDate = false;
        boolean isArray = false;

        for (NpTypeInterface schemaColumn : columnList) {
            NpDataType npDataType = NpDataType.parseString(schemaColumn.getColumnType());
            switch (npDataType) {
                case DECIMAL:
                    if (!isDecimal) {
                        stringBuilder.append("import").append(SPACE).append("java.math.BigDecimal").append(FRN);
                        isDecimal = true;
                    }
                    break;
                case DATE:
                    if (!isDate) {
                        stringBuilder.append("import").append(SPACE).append("java.util.Date").append(FRN);
                        isDate = true;
                    }
                    break;
                case ARRAY_STR:
                case ARRAY_INT:
                    log.info("List {}", npDataType.toString());
                    if (!isArray) {
                        stringBuilder.append("import").append(SPACE).append("java.util.List").append(FRN);
                        isArray = true;
                    }
                    break;
            }
        }
    }

    private static String extTable(SchemaTable table) {
        //@ExtTable(comment = "测试",abbr = "STT",export = "2000",traceable = false,queryTraceable = false,dataAuth = false)
        StringBuilder builder = new StringBuilder("@ExtTable(");
        builder.append("comment = \"").append(table.getTableComment()).append("\"");
        builder.append(",abbr = \"").append(table.getTableAbbr()).append("\"");
        builder.append(",export = \"").append(table.getExportNum()).append("\"");
        if (table.getIsTraceable()) {
            builder.append(",traceable = true");
        }
        if (table.getIsQueryTraceable()) {
            builder.append(",queryTraceable = true");
        }
        if (table.getIsAuth()) {
            builder.append(",dataAuth = true");
        }

        builder.append(")");
        return builder.toString();
    }

    private static String extColumn(SchemaColumn schemaColumn) {
        //@ExtColumn(comment = "测试", type = "XX", relation = "N", isTenantOnly = false, isAllOnly = false, isNull = true, isRealTime = false, isAuth = false, defaultOrder = "N")
        StringBuilder builder = new StringBuilder("@ExtColumn(");
        builder.append("comment = \"").append(schemaColumn.getColumnComment()).append("\"");
        builder.append(",type = \"").append(schemaColumn.getColumnType()).append("\"");
        if (!"N".equals(schemaColumn.getRender())) {
            builder.append(",relation = \"").append(schemaColumn.getRender()).append("\"");
        }
        if (schemaColumn.getIsTenantOnly()) {
            builder.append(",isTenantOnly = true");
        }
        if (schemaColumn.getIsAllOnly()) {
            builder.append(",isAllOnly = true");
        }
        if (!schemaColumn.getIsNull()) {
            builder.append(",isNull = false");
        }

        if (schemaColumn.getIsRealTime()) {
            builder.append(",isRealTime = true");
        }
        if (schemaColumn.getIsAuth()) {
            builder.append(",isAuth = true");
        }
        if (!"N".equals(schemaColumn.getDefaultOrder())) {
            builder.append(",defaultOrder = \"").append(schemaColumn.getDefaultOrder()).append("\"");
        }


        builder.append(")");
        return builder.toString();
    }

    public static String generate(String packageStr, SchemaTable table, List<SchemaColumn> columnList) {
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
        stringBuilder.append("//").append(table.getTableComment()).append(SPACE).append(table.getTableAbbr()).append(RN);
        stringBuilder.append("@Getter").append(RN);
        stringBuilder.append("@Setter").append(RN);
        stringBuilder.append("@Table(name = \"").append(table.getTableName()).append("\")").append(RN);

        if (!table.getTableName().startsWith("schema"))
            stringBuilder.append(extTable(table)).append(RN);


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
        stringBuilder.append(SPACE4).append("public static final TableDefinition NP_NAME = TableDefinition.table(\"")
                .append(table.getTableName()).append("\",\"").append(table.getTableAbbr()).append("\")").append(FRN);

        StringBuilder createCodeHelper = new StringBuilder();
        StringBuilder defaultCodeHelper = new StringBuilder();
        Set<String> sysColumnsValueSet = SysColumn.getValueSet();
        for (int i = 0; i < columnList.size(); i++) {
            SchemaColumn schemaColumn = columnList.get(i);
            stringBuilder.append(RN);
            stringBuilder.append(SPACE4).append("//").append(schemaColumn.getColumnComment()).append(RN);
            //public static final ColumnDefinition $tenantId = ColumnDefinition.name("tenant_id");
            stringBuilder.append(SPACE4).append("public static final ColumnDefinition $").append(toCamel(schemaColumn.getColumnName(), false))
                    .append(" = ColumnDefinition.tableAndColumn(\"").append(schemaColumn.getTableName()).append("\",\"").append(schemaColumn.getColumnName()).append("\")").append(FRN);

            if (SysColumn.sys_id.toString().equals(schemaColumn.getColumnName())) {
                stringBuilder.append(SPACE4).append("@Id").append(RN);
            }
            stringBuilder.append(SPACE4).append("@Column(name = \"").append(schemaColumn.getColumnName()).append("\")").append(RN);

            if (!table.getTableName().startsWith("schema"))
                stringBuilder.append(SPACE4).append(extColumn(schemaColumn)).append(RN);

            stringBuilder.append(SPACE4).append("private ").append(NpDataType.getFromType(schemaColumn.getColumnType())).append(SPACE).append(toCamel(schemaColumn.getColumnName(), false)).append(FRN);

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
