package java.persistence;

import javax.persistence.EnumType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.persistence.EnumType.ORDINAL;


/**
 * @author Launcel
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Enumerated {

    /**
     * (Optional) The type used in mapping an enum type.
     */
    EnumType value() default ORDINAL;
}
