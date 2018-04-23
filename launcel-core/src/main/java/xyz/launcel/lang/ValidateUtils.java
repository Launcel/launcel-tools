package xyz.launcel.lang;

import xyz.launcel.annotation.Limit;
import xyz.launcel.annotation.Types;
import xyz.launcel.ensure.Me;

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

//        if (l.group().length > 0 && l.excGroup().length > 0)
//            ExceptionFactory.error("_DEFINE_ERROR_CODE_013", "Limit校验不能同时出现group和exceptGroup");
//        // 全部校验
//        if (l.group().length <= 0 && l.excGroup().length <= 0)
//            checkFiled(value, l);//,f);
//        else if (l.group().length <= 0 && l.excGroup().length > 0)
//            validateExceptGroup(f, value, group);
//        else if (l.excGroup().length <= 0 && l.group().length > 0) {
//            for (Class<?> aClass : l.group())
//                if (aClass.getSimpleName().equals(group))
//                    checkFiled(value, l);//,f);
//        }
        if (l.group().length > 0) {
            for (Class<?> aClass : l.group())
                if (aClass.getSimpleName().equals(group))
                    checkFiled(value, l);//,f);
        } else // 全部校验
            checkFiled(value, l);//,f);
    }


//    private static void validateExceptGroup(Field f, Object value, String group) {
//        Limit l = f.getAnnotation(Limit.class);
//        for (Class<?> clazz : l.excGroup()) {
//            if (clazz.getSimpleName().equals(group)) return;
//            checkFiled(value, l);//,f);
//        }
//
//    }
    
    private static void checkFiled(Object value, Limit l) {//,Field f) {
        String message = l.message();
        Types  type    = l.type();
        verifyType(value, message, type);//, f.getName());
    }
    
    private static void verifyType(Object o, String message, Types type) {//, String name) {
        switch (type) {
            case string:
                Me.that((String) o).isBlank(message);
                break;
            case number:
                Me.that(RegUtil.isNum(o)).isFalse(message);
                break;
            case ip:
                Me.that(RegUtil.isIP(o)).isFalse(message);
                break;
            case decimal:
                Me.that(RegUtil.isFloatNum(o)).isFalse(message);
                break;
            case email:
                Me.that(RegUtil.isEmail(o)).isFalse(message);
                break;
            case tel:
                Me.that(RegUtil.isMobile(o)).isFalse(message);
                break;
            case qq:
                Me.that(RegUtil.isQQ(o)).isFalse(message);
                break;
            default:
                break;
        }
    }
}
