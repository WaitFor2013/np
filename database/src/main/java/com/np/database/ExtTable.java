package com.np.database;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface ExtTable {

    String comment();

    String abbr();

    String export();

    boolean traceable() default false;

    boolean queryTraceable() default false;

    boolean dataAuth() default false;
    
}
