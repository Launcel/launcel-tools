package javax.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Launcel
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {
}
