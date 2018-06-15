package xyz.launcel.lang;

import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.exception.SystemException;

import java.util.Objects;
import java.util.regex.Pattern;

public interface RegUtil
{

    static boolean isTrue(String text, String pattern)
    {
        if (Objects.isNull(text))
        {
            throw new SystemException("_DEFINE_ERROR_CODE_011", "正则校验数据为空");
        }
        return Pattern.compile(pattern).matcher(text).matches();
    }

    /**
     * 是否正整数
     *
     * @param text param
     *
     * @return true or false
     */
    static boolean isNum(String text)
    {
        return isTrue(text, "^-?[1-9]\\d*$");
    }

    /**
     * 是否正浮点数
     *
     * @param text param
     *
     * @return true or false
     */
    static boolean isFloatNum(String text)
    {
        return isTrue(text, "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$");
    }

    /**
     * 是否手机号
     *
     * @param text param
     *
     * @return true or false
     */
    static boolean isMobile(String text)
    {
        return isTrue(text, "0?(13|14|15|17|18|19)[0-9]{9}");
    }

    /**
     * 是否email
     *
     * @param text param
     *
     * @return true or false
     */
    static boolean isEmail(String text)
    {
        return isTrue(text, "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}");
    }

    /**
     * 是否身份证
     *
     * @param text param
     *
     * @return true or false
     */
    static boolean isShengfenZheng(String text)
    {
        return isTrue(text, "\\d{17}[\\d|x]|\\d{15}");
    }

    /**
     * 是否QQ
     *
     * @param text param
     *
     * @return true or false
     */
    static boolean isQQ(String text)
    {
        return isTrue(text, "[1-9]([0-9]{5,11})");
    }

    /**
     * 是否 url
     *
     * @param text param
     *
     * @return true or false
     */
    static boolean isUrl(String text)
    {
        return isTrue(text, "^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+");
    }

    /**
     * 是否 IP
     *
     * @param text param
     *
     * @return true or false
     */
    static boolean isIP(String text)
    {
        return isTrue(text, "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)");
    }
}
