package xyz.launcel.exception;

/**
 * @author Launcel
 */
public class ExceptionFactory {

    private ExceptionFactory() {
    }

    public static void create(String message) {
        throw new ProfessionException(ExceptionHelp.getMessage(message));
    }

    public static void error(String message) {
        throw new SystemError("SYSTEM ERROR :" + ExceptionHelp.getMessage(message));
    }

    public static void error(String msg, String msgInfo) {
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        throw new SystemError(sb);
    }

    public static void create(String msg, String msgInfo) {
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        throw new ProfessionException(sb);
    }
}
