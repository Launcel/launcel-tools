package xyz.launcel.exception;

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
        throw new ProfessionException(code, msg);
    }


    static void error(String code)
    {
        Map<String, String> map = ExceptionHelp.getMessage(code);
        error(code, map.get(code));
    }

    static void error(String code, String msg)
    {
        throw new SystemException(code, msg);
    }
}
