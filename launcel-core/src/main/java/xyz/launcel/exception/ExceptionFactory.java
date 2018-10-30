package xyz.launcel.exception;

import xyz.launcel.log.RootLogger;

import java.util.Map;

/**
 * @author Launcel
 */
public interface ExceptionFactory
{


    static void create(String code)
    {
        Map<String, String> map = ExceptionHelp.getMessage(code);
        create(code, map.get(code));
    }

    static void create(String code, String msg)
    {
        String sb = "[" + code + " : " + msg + "]";
        RootLogger.error(sb);
        throw new ProfessionException(msg);
    }
}
