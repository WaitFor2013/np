package com.np.database.orm;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface TablePrefix {

    /**
     * column table prefix
     *
     * @return
     */
    String value() default "";
}
