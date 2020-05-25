package com.np.database.orm.type;

import java.io.Reader;
import java.sql.*;


/**
 * The {@link TypeHandler} for {@link Clob}/{@link Reader} using method supported at JDBC 4.0.
 * @since 3.4.0
 * @author Kazuki Shimizu
 */
public class ClobReaderTypeHandler extends BaseTypeHandler<Reader> {

  /**
   * Set a {@link Reader} into {@link PreparedStatement}.
   * @see PreparedStatement#setClob(int, Reader)
   */
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, Reader parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setClob(i, parameter);
  }

  /**
   * Get a {@link Reader} that corresponds to a specified column name from {@link ResultSet}.
   * @see ResultSet#getClob(String)
   */
  @Override
  public Reader getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    return toReader(rs.getClob(columnName));
  }

  /**
   * Get a {@link Reader} that corresponds to a specified column index from {@link ResultSet}.
   * @see ResultSet#getClob(int)
   */
  @Override
  public Reader getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    return toReader(rs.getClob(columnIndex));
  }

  /**
   * Get a {@link Reader} that corresponds to a specified column index from {@link CallableStatement}.
   * @see CallableStatement#getClob(int)
   */
  @Override
  public Reader getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return toReader(cs.getClob(columnIndex));
  }

  private Reader toReader(Clob clob) throws SQLException {
    if (clob == null) {
      return null;
    } else {
      return clob.getCharacterStream();
    }
  }

}
