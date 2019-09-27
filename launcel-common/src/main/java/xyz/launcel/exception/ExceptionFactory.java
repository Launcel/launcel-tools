package xyz.launcel.exception;

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
        throw new BusinessException(code, msg);
    }

    static void error(String code)
    {
        var map = ExceptionHelp.getMessage(code);
        error(code, map.get(code));
    }

    static void error(String code, String msg)
    {
        throw new SystemException(code, msg);
    }
}
