package xyz.launcel.lang;

import xyz.launcel.ensure.Me;
import xyz.launcel.exception.ExceptionFactory;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegUtil {
    private static boolean isTrue(Object text, String pattern) {
        if (Objects.isNull(text))
            ExceptionFactory.create("_DEFINE_ERROR_CODE_008", "正则校验数据为空");
        return Pattern.compile(pattern).matcher(text.toString()).matches();
    }

    /**
     * 是否正整数
     *
     * @param text
     * @return
     */
    public static boolean isNum(Object text) {
        return isTrue(text, "^[1-9]\\d*$");
    }

    /**
     * 是否正浮点数
     *
     * @param text
     * @return
     */
    public static boolean isFloatNum(Object text) {
        return isTrue(text, "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$");
    }

    /**
     * 是否手机号
     *
     * @param text
     * @return
     */
    public static boolean isMobile(Object text) {
        return isTrue(text, "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
    }

    /**
     * 是否email
     *
     * @param text
     * @return
     */
    public static boolean isEmail(Object text) {
        return isTrue(text, "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
    }

    /**
     * 是否身份证
     *
     * @param text
     * @return
     */
    public static boolean isShengfenZheng(Object text) {
        return isTrue(text, "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$");
    }

    /**
     * 是否QQ
     *
     * @param text
     * @return
     */
    public static boolean isQQ(Object text) {
        return isTrue(text, "[1-9][0-9]{4,}");
    }

    /**
     * 是否 url
     *
     * @param text
     * @return
     */
    public static boolean isUrl(Object text) {
        return isTrue(text, "[a-zA-z]+://[^\\s]*");
    }

    /**
     * 是否 IP
     *
     * @param text
     * @return
     */
    public static boolean isIP(Object text) {
        return isTrue(text, "(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)){3}");
    }
}
