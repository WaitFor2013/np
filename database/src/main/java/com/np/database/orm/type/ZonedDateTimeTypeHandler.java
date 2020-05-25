package com.np.database.orm.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

/**
 * @since 3.4.5
 * @author Tomas Rohovsky
 */
public class ZonedDateTimeTypeHandler extends BaseTypeHandler<ZonedDateTime> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, ZonedDateTime parameter, JdbcType jdbcType)
          throws SQLException {
    ps.setObject(i, parameter);
  }

  @Override
  public ZonedDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return rs.getObject(columnName, ZonedDateTime.class);
  }

  @Override
  public ZonedDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return rs.getObject(columnIndex, ZonedDateTime.class);
  }

  @Override
  public ZonedDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return cs.getObject(columnIndex, ZonedDateTime.class);
  }
}
