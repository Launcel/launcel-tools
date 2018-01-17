package xyz.launcel.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Launcel in 2017/9/27
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limit
{

    /**
     * 当前字段是否必需
     * default false
     *
     * @return
     */
    //    boolean notBlank() default true;

    String message() default "_DEFINE_ERROR_CODE_001";

    /**
     * 当前字段的类型
     *
     * @return
     *
     * @see Types
     * default string
     */
    Types type() default Types.string;

    /**
     * 校验字段对应的 group
     *
     * @return
     *
     * @see Group
     */
    Class<?>[] group() default {};

    Class<?>[] excGroup() default {};

}
