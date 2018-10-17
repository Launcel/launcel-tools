package xyz.launcel.lang;

import xyz.launcel.annotation.Limit;
import xyz.launcel.ensure.Me;
import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.exception.ProfessionException;
import xyz.launcel.log.RootLogger;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;

public final class ValidateUtils
{

    private ValidateUtils() { }

    public static void validateLimit(Object object, String group)
    {
        var fields = object.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Limit.class))
            {
                try
                {
                    var value = field.get(object);
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
        var clazz  = parameter.getType();
        var fields = clazz.getDeclaredFields();
        Arrays.stream(fields).forEach(field -> {
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
        var l = f.getAnnotation(Limit.class);
        if (l.group().length < 0)
        {
            checkFiled(value, l, f);
            return;
        }
        Arrays.stream(l.group()).forEach(aClass -> {
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
        var msg = name;
        var o   = temp.toString();
        try
        {
            switch (l.type())
            {
                case STRING:
                    if (StringUtils.isBlank(o))
                    { msg += " : 不能为空"; }
                    break;
                case NUMBER:
                    if (!RegUtil.isNum(o))
                    { msg += " : 不是整数"; }
                    else
                    { checkValue(o, l, name); }
                    break;
                case IP:
                    if (!RegUtil.isIP(o))
                    { msg += " : 不是合法ip"; }
                    break;
                case DECIMAL:
                    if (!RegUtil.isFloatNum(o))
                    { msg += " : 不是小数"; }
                    else
                    { checkValue(o, l, name); }
                    break;
                case EMAIL:
                    if (!RegUtil.isEmail(o))
                    { msg += " : 不是邮箱"; }
                    break;
                case TEL:
                    if (!RegUtil.isMobile(o))
                    { msg += " : 不是合法手机号码"; }
                    break;
                case QQ:
                    if (!RegUtil.isQQ(o))
                    { msg += " : 不是合法QQ号码"; }
                    break;
                case LIST:
                    if (Objects.isNull(o) || CollectionUtils.sizeIsEmpty(o))
                    {
                        msg += " : 数组无效";
                    }
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
            Me.builder(Double.valueOf(l.minValue()).compareTo(Double.valueOf(value)) < 0).isTrue(name + "小于" + l.minValue());
        }
        if (StringUtils.isNotBlank(l.maxValue()))
        {
            Me.builder(Double.valueOf(l.maxValue()).compareTo(Double.valueOf(value)) > 0).isTrue(name + "大于" + l.maxValue());
        }
    }
}
