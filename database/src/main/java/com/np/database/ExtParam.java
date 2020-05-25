package com.np.database;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface ExtParam {

    String tablePrefix() default "";

    String tableName() default "";

    String columnName();

    String columnType();

    String columnComment();

    boolean isParam() default false;

    boolean isResult() default true;
}
