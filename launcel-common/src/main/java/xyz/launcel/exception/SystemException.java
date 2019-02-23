package xyz.launcel.exception;

/**
 * 默认框架的异常代码为 _DEFINE_ERROR_CODE_011
 */
public class SystemException extends AbstractException
{
    private static final long serialVersionUID = -1597189035906555024L;

    protected SystemException()
    {
        super();
    }

    SystemException(String message)
    {
        super(message);
    }

    public SystemException(String code, String message)
    {
        super(code, message);
    }
}
