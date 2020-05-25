package com.np.database.orm.type;

import com.np.database.exception.NpDbException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Slf4j
public class ListTypeHandler implements TypeHandler<List<?>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<?> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            try {
                ps.setNull(i, JdbcType.ARRAY.TYPE_CODE);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                        + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. "
                        + "Cause: " + e, e);
            }
        } else {
            try {

                //Array arrayOf = ps.getConnection().createArrayOf(jdbcType.name(), parameter.toArray());
                //ps.setArray(i, arrayOf);

                //log.info("参数*************************{}",arrayOf.toString());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("{");
                for (int tp = 0; tp < parameter.size(); tp++) {
                    if (tp != 0) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(parameter.get(tp));
                }
                stringBuilder.append("}");

                ps.setString(i, stringBuilder.toString());
            } catch (Exception e) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType
                        + " . "
                        + "Try setting a different JdbcType for this parameter or a different configuration property. "
                        + "Cause: " + e, e);
            }
        }

    }

    @Override
    public List<?> getResult(ResultSet rs, String columnName) throws SQLException {
        List<?> result;
        try {
            Array array = rs.getArray(columnName);
            result = array == null ? null : new ArrayList<>(Arrays.asList((Object[]) array.getArray()));
        } catch (Exception e) {
            throw new NpDbException(
                    "Error attempting to get column '" + columnName + "' from result list.  Cause: " + e, e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public List<?> getResult(ResultSet rs, int columnIndex) throws SQLException {
        List<?> result;
        try {
            Array array = rs.getArray(columnIndex);
            result = array == null ? null : new ArrayList<>(Arrays.asList((Object[]) array.getArray()));
        } catch (Exception e) {
            throw new NpDbException(
                    "Error attempting to get column #" + columnIndex + " from result list.  Cause: " + e, e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public List<?> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        List<?> result;
        try {
            Array array = cs.getArray(columnIndex);
            result = array == null ? null : new ArrayList<>(Arrays.asList((Object[]) array.getArray()));
        } catch (Exception e) {
            throw new NpDbException(
                    "Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + e, e);
        }
        if (cs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

}