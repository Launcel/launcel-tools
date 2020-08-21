package xyz.launcel.common.exception;

/**
 * 默认框架的异常代码为 _DEFINE_ERROR_CODE_012
 */
public class BusinessException extends AbstractException
{

    private static final long serialVersionUID = -8971428275453038218L;

    public BusinessException(String message)
    {
        this(null, message);
    }

    public BusinessException(String code, String msg)
    {
        super(code, msg);
    }
}
