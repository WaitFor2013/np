package com.np.design.domain.misc;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface GridCellInit {

    String string() default "";

    String intString() default "";

    String boolString() default "";
}
