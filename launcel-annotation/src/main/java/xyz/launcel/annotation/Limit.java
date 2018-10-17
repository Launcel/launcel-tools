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
     * @return message
     */
    String message() default "_PARAM_ERROR_CODE_001";

    /**
     * 当前字段的类型
     *
     * @see Types
     * default string
     */
    Types type() default Types.STRING;

    /**
     * 校验字段对应的 group
     *
     * @see Group
     */
    Class<?>[] group() default {};

    String minValue() default "";

    String maxValue() default "";

}
