package javax.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Launcel
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueConstraint
{
    String name() default "";

    String[] columnNames();
}
