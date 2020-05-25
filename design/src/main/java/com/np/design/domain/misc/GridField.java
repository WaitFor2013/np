package com.np.design.domain.misc;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface GridField {

    String name() default "";

    String maxWidth() default "";

    String minWidth() default "";

    String dict() default "";

    String editable() default "true";
}
