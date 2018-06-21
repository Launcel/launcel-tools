package xyz.launcel.lang;

import xyz.launcel.annotation.Limit;
import xyz.launcel.annotation.Types;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.log.RootLogger;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public final class ValidateUtils {
    
    private ValidateUtils() {
    }
    
    public static void validateLimit(Object object, String group) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Limit.class)) {
                Object value = field.get(object);
                validateGroup(field, value, group);
            }
            field.setAccessible(false);
        }
    }
    
    @Deprecated
    public static void validateLimit(Parameter parameter, String group) throws ReflectiveOperationException {
        Class<?> clazz  = parameter.getType();
        Field[]  fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Limit.class))
                validateGroup(field, clazz.newInstance(), group);
            field.setAccessible(false);
        }
    }
    
    private static void validateGroup(Field f, Object value, String group) {
        Limit l = f.getAnnotation(Limit.class);
        if (l.group().length > 0) {
            for (Class<?> aClass : l.group())
                if (aClass.getSimpleName().equals(group))
                    checkFiled(value, l, f);
        } else // 全部校验
            checkFiled(value, l, f);
    }
    
    private static void checkFiled(Object value, Limit l, Field f) {
        String message = l.message();
        Types  type    = l.type();
        verifyType(value, message, type, f.getName());
    }
    
    private static void verifyType(Object o, String message, Types type, String name) {
        String msg = name;
        switch (type) {
            case string:
                if (StringUtils.isBlank((String) o)) {
                    msg += "不能为空";
                }
                break;
            case number:
                if (!RegUtil.isNum(o)) {
                    msg += "不是整数";
                }
                break;
            case ip:
                if (!RegUtil.isIP(o)) {
                    msg += "不是合法ip";
                }
                break;
            case decimal:
                if (!RegUtil.isFloatNum(o)) {
                    msg += "不是小数";
                }
                break;
            case email:
                if (!RegUtil.isEmail(o)) {
                    msg += "不能为空";
                }
                break;
            case tel:
                if (!RegUtil.isMobile(o)) {
                    msg += "不是合法手机号码";
                }
                break;
            case qq:
                if (!RegUtil.isQQ(o)) {
                    msg += "不是合法QQ号码";
                }
                break;
            default:
                break;
        }
        if (!msg.equals(name)) {
            RootLogger.error(msg);
            ExceptionFactory.create(message);
        }
    }
}
