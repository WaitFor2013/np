package com.np.database.orm.type;

import java.lang.annotation.*;

/**
 * The annotation that specify jdbc types to map {@link TypeHandler}.
 *
 * <p><br>
 * <b>How to use:</b>
 * <pre>
 * &#064;MappedJdbcTypes({JdbcType.CHAR, JdbcType.VARCHAR})
 * public class StringTrimmingTypeHandler implements TypeHandler&lt;String&gt; {
 *   // ...
 * }
 * </pre>
 * @author Eduardo Macarron
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedJdbcTypes {
  /**
   * Returns jdbc types to map {@link TypeHandler}.
   *
   * @return jdbc types
   */
  JdbcType[] value();

  /**
   * Returns whether map to jdbc null type.
   *
   * @return {@code true} if map, {@code false} if otherwise
   */
  boolean includeNullJdbcType() default false;
}
