package com.np.design.domain;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class SqlFile {

    private static ThreadLocal<DateFormat> YYMMDD = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    private static ThreadLocal<DateFormat> YYMMDDHHMMSS = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };


    public static String mapRow(ResultSet rs) throws SQLException {

        StringBuilder sb = new StringBuilder();
        ResultSetMetaData metaData = rs.getMetaData();
        String tableName = metaData.getTableName(1);
        sb.append("INSERT INTO ").append(tableName);
        Map<String, String> pair = getColumnsPair(rs);
        StringBuffer firstSb = new StringBuffer();
        StringBuffer lastSb = new StringBuffer();
        for (Map.Entry<String, String> entry : pair.entrySet()) {
            firstSb.append(entry.getKey()).append(",");
            lastSb.append(entry.getValue()).append(",");
        }
        firstSb.deleteCharAt(firstSb.length() - 1);
        lastSb.deleteCharAt(lastSb.length() - 1);
        sb.append(" ( ").append(firstSb).append(" ) VALUES ( ").append(lastSb).append(" );");

        return sb.toString();
    }


    private static Map<String, String> getColumnsPair(ResultSet rs) throws SQLException {
        Map<String, String> result = new HashMap<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            result.put(metaData.getColumnName(i),
                    getColumnValue(metaData.getColumnType(i), i, rs));
        }

        return result;
    }

    private static String getColumnValue(int columnType, int i, ResultSet rs) throws SQLException {
        switch (columnType) {
            case Types.NUMERIC:
                return rs.getLong(i) + "";
            case Types.VARCHAR:

                if (rs.getString(i) == null)
                    return null;

                String str = rs.getString(i);
                str = str.replaceAll("'", "''");

                return "'" + str + "'";
            case Types.DATE:
                return (rs.getDate(i) == null) ? null : "'" + YYMMDD.get().format(rs.getDate(i)) + "'";
            case Types.TIMESTAMP:
                return (rs.getTimestamp(i) == null) ? null : "'" + YYMMDDHHMMSS.get().format(rs.getTimestamp(i)) + "'";
            //case Types.TIME:return rs.getTime(i);
            case Types.BOOLEAN:
                return rs.getBoolean(i) + "";
            //case Types.ARRAY :return rs.getArray(i);
            case Types.BIGINT:
                return rs.getLong(i) + "";
            //case Types.BINARY:return rs.getBinaryStream(i);
            //case Types.BLOB:return rs.getBlob(i);
            case Types.CHAR:
                return (rs.getString(i) == null) ? null : "'" + rs.getString(i) + "'";
            case Types.INTEGER:
                return rs.getInt(i) + "";
            case Types.DOUBLE:
                return rs.getDouble(i) + "";
            case Types.FLOAT:
                return rs.getFloat(i) + "";
            case Types.SMALLINT:
                return rs.getInt(i) + "";
            case Types.TINYINT:
                return rs.getInt(i) + "";
            case Types.DECIMAL:
                return rs.getLong(i) + "";
            case Types.BIT:
                return (null == rs.getObject(i)) ? null : rs.getBoolean(i) ? "'t'" : "'f'";
            default:
                return null;
        }
    }
}
