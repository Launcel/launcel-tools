package xyz.launcel.lang;

import xyz.launcel.annotation.Limit;
import xyz.launcel.ensure.Me;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.exception.ProfessionException;
import xyz.launcel.log.RootLogger;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;

public final class ValidateUtils
{

    private ValidateUtils() { }

    public static void validateLimit(Object object, String group)
    {
        Field[] fields = object.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field ->
        {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Limit.class))
            {
                try
                {
                    Object value = field.get(object);
                    validateGroup(field, value, group);
                }
                catch (IllegalAccessException x)
                {
                    RootLogger.error(x.getLocalizedMessage());
                }
            }
            field.setAccessible(false);
        });
    }

    @Deprecated
    public static void validateLimit(Parameter parameter, String group)
    {
        Class<?> clazz  = parameter.getType();
        Field[]  fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(field ->
        {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Limit.class))
            {
                try
                {
                    validateGroup(field, clazz.newInstance(), group);
                }
                catch (ReflectiveOperationException x)
                {
                    RootLogger.error(x.getLocalizedMessage());
                }
            }
            field.setAccessible(false);
        });
    }

    private static void validateGroup(Field f, Object value, String group)
    {
        Limit l = f.getAnnotation(Limit.class);
        if (l.group().length < 0)
        {
            checkFiled(value, l, f);
            return;
        }
        Arrays.stream(l.group()).forEach(aClass ->
        {
            if (aClass.getSimpleName().equals(group))
            {
                checkFiled(value, l, f);
            }
        });
    }

    private static void checkFiled(Object value, Limit l, Field f)
    {
        verifyType(value, l, f.getName());
    }

    private static void verifyType(Object temp, Limit l, String name)
    {
        String msg = name;
        String o   = temp.toString();
        try
        {
            switch (l.type())
            {
                case string:
                    if (StringUtils.isBlank(o))
                    { msg += " : 不能为空"; }
                    break;
                case number:
                    if (!RegUtil.isNum(o))
                    { msg += " : 不是整数"; }
                    else
                    { checkValue(o, l, name); }
                    break;
                case ip:
                    if (!RegUtil.isIP(o))
                    { msg += " : 不是合法ip"; }
                    break;
                case decimal:
                    if (!RegUtil.isFloatNum(o))
                    { msg += " : 不是小数"; }
                    else
                    { checkValue(o, l, name); }
                    break;
                case email:
                    if (!RegUtil.isEmail(o))
                    { msg += " : 不是邮箱"; }
                    break;
                case tel:
                    if (!RegUtil.isMobile(o))
                    { msg += " : 不是合法手机号码"; }
                    break;
                case qq:
                    if (!RegUtil.isQQ(o))
                    { msg += " : 不是合法QQ号码"; }
                    break;
                default:
                    break;
            }
        }
        catch (ProfessionException x)
        {
            if (!msg.equals(name))
            {
                RootLogger.error(msg);
                ExceptionFactory.create(l.message());
            }
        }
    }

    private static void checkValue(String value, Limit l, String name)
    {

        if (StringUtils.isNotBlank(l.minValue()))
        {
            Me.that(Double.valueOf(l.minValue()).compareTo(Double.valueOf(value)) < 0).isTrue(name + "小于" + l.minValue());
        }
        if (StringUtils.isNotBlank(l.maxValue()))
        {
            Me.that(Double.valueOf(l.maxValue()).compareTo(Double.valueOf(value)) > 0).isTrue(name + "大于" + l.maxValue());
        }
    }
}
