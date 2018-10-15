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
        var map = ExceptionHelp.getMessage(code);
        create(code, map.get(code));
    }

    static void create(String code, String msg)
    {
        var sb = "[" + code + " : " + msg + "]";
        RootLogger.error(sb);
        throw new ProfessionException(msg);
    }
}
