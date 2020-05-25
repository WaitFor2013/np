
package com.np.database.orm.type;

import java.lang.annotation.*;

/**
 * The annotation that specify alias name.
 *
 * <p><br>
 * <b>How to use:</b>
 * <pre>
 * &#064;Alias("Email")
 * public class UserEmail {
 *   // ...
 * }
 * </pre>
 * @author Clinton Begin
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Alias {
  /**
   * Return the alias name.
   *
   * @return the alias name
   */
  String value();
}
