package xyz.launcel.exception;

/**
 * 默认框架的异常代码为 _DEFINE_ERROR_CODE_012
 */
public class ProfessionException extends AbstractException
{

    private static final long serialVersionUID = -8971428275453038218L;

    ProfessionException(String message)
    {
        super(message);
    }

    ProfessionException(String code, String msg)
    {
        super(code, msg);
    }

}
