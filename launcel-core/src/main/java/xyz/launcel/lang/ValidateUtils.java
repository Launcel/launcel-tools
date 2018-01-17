package xyz.launcel.lang;

import xyz.launcel.annotation.Limit;
import xyz.launcel.annotation.Types;
import xyz.launcel.ensure.Me;
import xyz.launcel.exception.ExceptionFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

public final class ValidateUtils {

    public static String getMethodGroupAnnotation(String method) {
        return method.substring(0, 1).toUpperCase() + method.substring(1);
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
        Class<?> clazz = parameter.getType();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Limit.class))
                validateGroup(field, clazz.newInstance(), group);
            field.setAccessible(false);
        }
    }

    private static void validateGroup(Field f, Object value, String group) {
        Limit l = f.getAnnotation(Limit.class);

        if (l.group().length > 0 && l.excGroup().length > 0)
            ExceptionFactory.error("_DEFINE_ERROR_CODE_013");
        // 全部校验
        if (l.group().length <= 0 && l.excGroup().length <= 0)
            checkFiled(value, l);//,f);
        else if (l.group().length <= 0 && l.excGroup().length > 0)
            validateExceptGroup(f, value, group);
        else if (l.excGroup().length <= 0 && l.group().length > 0) {
            for (Class<?> aClass : l.group())
                if (aClass.getSimpleName().equals(group))
                    checkFiled(value, l);//,f);
        }
    }


    private static void validateExceptGroup(Field f, Object value, String group) {
        Limit l = f.getAnnotation(Limit.class);
        for (Class<?> clazz : l.excGroup()) {
            if (clazz.getSimpleName().equals(group)) return;
            checkFiled(value, l);//,f);
        }

    }

    private static void checkFiled(Object value, Limit l) {//,Field f) {
        String message = l.message();
        Types typeClassName = l.type();
        verifyType(value, message, typeClassName);//, f.getName());
    }

    private static void verifyType(Object o, String message, Types typeClassName) {//, String name) {
        if (typeClassName == Types.string)
            Me.that((String) o).isBlank(message);
        else if (typeClassName == Types.number)
            Me.that(RegUtil.isNum(o)).isFalse(message);
        else if (typeClassName == Types.ip)
            Me.that(RegUtil.isIP(o)).isFalse(message);
        else if (typeClassName == Types.decimal)
            Me.that(RegUtil.isFloatNum(o)).isFalse(message);
        else if (typeClassName == Types.email)
            Me.that(RegUtil.isEmail(o)).isFalse(message);
        else if (typeClassName == Types.tel)
            Me.that(RegUtil.isMobile(o)).isFalse(message);
        else if (typeClassName == Types.qq)
            Me.that(RegUtil.isQQ(o)).isFalse(message);
    }


}
