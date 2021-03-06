package javax.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Launcel
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table
{
    String name() default "";

    String catalog() default "";

    String schema() default "";

    UniqueConstraint[] uniqueConstraints() default {};

    Index[] indexes() default {};

    String describe() default "";
}
