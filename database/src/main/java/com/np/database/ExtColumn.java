package com.np.database;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface ExtColumn {

    String comment();

    String type();

    String relation() default "N";

    boolean isTenantOnly() default false;

    boolean isAllOnly() default false;

    boolean isNull() default true;

    boolean isRealTime() default false;

    boolean isAuth() default false;

    String defaultOrder() default "N";

}
