package com.np.design.domain.ddl;

import com.np.database.NpDataType;
import com.np.database.orm.session.DefaultSqlSession;
import com.np.database.recommend.SysColumn;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.domain.db.SchemaTable;
import com.np.design.domain.misc.PgType;
import com.np.design.exception.NpException;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.List;

public class PostgresDDLHelper {

    public static String pgType(NpDataType dataType) {

        switch (dataType) {
            case BOOL:
                return " BOOLEAN ";

            case INT:
                return " INT ";
            case LONG:
                return " BIGINT ";

            case STR64:
                return " VARCHAR(64) ";
            case STR256:
                return " VARCHAR(256) ";
            case STR1024:
                return " VARCHAR(1024) ";
            case TEXT:
                return " TEXT ";

            case DECIMAL:
                return " NUMERIC(19, 4) ";

            case DATE:
                return " TIMESTAMP ";

            case ARRAY_STR:
                return " TEXT[] ";
            case ARRAY_INT:
                return " INT[] ";

            case JSON:
                return " JSON ";

            default:
                ;

        }

        throw new NpException("NpDataType不支持");
    }

    public static String getColumn(SchemaColumn schemaColumn) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder
                .append(schemaColumn.getColumnName()).append(" ")
                .append(pgType(NpDataType.parseString(schemaColumn.getColumnType())));

        if (SysColumn.sys_id.toString().equals(schemaColumn.getColumnName())) {
            strBuilder.append(" PRIMARY KEY ");
        }

        strBuilder.append(schemaColumn.getIsNull() ? "" : " NOT NULL ");
        return strBuilder.toString();
    }

    public static Boolean isColumnChange(SchemaColumn dbColumn, SchemaColumn cgColumn) {
        //need ddl
        return !(dbColumn.getColumnType().equals(cgColumn.getColumnType())
                && dbColumn.getIsNull().equals(cgColumn.getIsNull()));
    }

    public static void createIndex(DefaultSqlSession sqlSession, SchemaTable schemaTable, List<String> tenantIndex, List<String> allIndex) {
        //create unique index idx_unq_tbl_unique_index_a_b on tbl_unique_index using btree (a,b);
        if (!allIndex.isEmpty()) {
            for (String index : allIndex) {
                String uqIdx = String.format(" create unique index %s_idx_unq_%s on %s  using btree (%s)"
                        , schemaTable.getTableName(), index, schemaTable.getTableName(), index);
                sqlSession.executeDDL(uqIdx);
            }
        }

        StringBuilder columnsIdxName = new StringBuilder(SysColumn.tenant_id.toString());
        StringBuilder columns = new StringBuilder(SysColumn.tenant_id.toString());
        for (String column : tenantIndex) {
            columnsIdxName.append("_").append(column);
            columns.append(",").append(column);
        }

        String uqIdx = String.format(" create unique index %s_idx_unq_%s on %s  using btree (%s)"
                , schemaTable.getTableName(), columnsIdxName.toString(), schemaTable.getTableName(), columns.toString());

        sqlSession.executeDDL(uqIdx);
    }

    public static void createSql(DefaultSqlSession sqlSessin, SchemaTable schemaTable, List<SchemaColumn> columns) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("CREATE TABLE ").append(schemaTable.getTableName());
        strBuilder.append("(").append("\n");

        for (int i = 0; i < columns.size(); i++) {
            SchemaColumn schemaColumn = columns.get(i);

            strBuilder.append(getColumn(schemaColumn));

            if (i != columns.size() - 1) {
                strBuilder.append(",");
            }
            strBuilder.append("\n");
        }

        strBuilder.append(")");

        sqlSessin.executeDDL(strBuilder.toString());
    }

    public static String generateCreateSQL(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("CREATE TABLE ").append(tableAnnotation.name());
        strBuilder.append("(").append("\n");
        Field[] declaredFields = clazz.getDeclaredFields();
        boolean isFirst = true;
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            Column annotation = field.getAnnotation(Column.class);
            PgType pgType = field.getAnnotation(PgType.class);
            //GridField gridField = field.getAnnotation(GridField.class);
            if (null != annotation) {
                if (!isFirst) {
                    strBuilder.append(",");
                } else {
                    isFirst = false;
                }
                strBuilder.append(annotation.name()).append(" ").append(pgType.column());
                strBuilder.append("\n");
            }
        }

        strBuilder.append(")");

        return strBuilder.toString();
    }

}
