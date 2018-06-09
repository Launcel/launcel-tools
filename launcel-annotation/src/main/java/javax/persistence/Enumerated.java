package javax.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * @author Launcel
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Enumerated
{

    /**
     * (Optional) The type used in mapping an enum type.
     */
    EnumType value() default EnumType.ORDINAL;
}
