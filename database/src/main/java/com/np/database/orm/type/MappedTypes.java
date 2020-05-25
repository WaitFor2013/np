package com.np.database.orm.type;

import java.lang.annotation.*;

/**
 * The annotation that specify java types to map {@link TypeHandler}.
 *
 * <p><br>
 * <b>How to use:</b>
 * <pre>
 * &#064;MappedTypes(String.class)
 * public class StringTrimmingTypeHandler implements TypeHandler&lt;String&gt; {
 *   // ...
 * }
 * </pre>
 * @author Eduardo Macarron
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MappedTypes {
  /**
   * Returns java types to map {@link TypeHandler}.
   *
   * @return java types
   */
  Class<?>[] value();
}
