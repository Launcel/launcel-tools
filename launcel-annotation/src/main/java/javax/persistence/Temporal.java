package javax.persistence;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <pre>
 *     Example:
 *
 *     &#064;Temporal(DATE)
 *     protected java.util.Date endDate;
 * </pre>
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface Temporal {
    /**
     * The type used in mapping <code>java.util.Date</code> or <code>java.util.Calendar</code>.
     */
    TemporalType value();
}
