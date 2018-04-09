package java.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Launcel
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {
    String name() default "";

    String columnList();

    boolean unique() default false;
}
