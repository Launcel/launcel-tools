package javax.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static javax.persistence.EnumType.STRING;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Enumerated {

    EnumType value() default STRING;
}