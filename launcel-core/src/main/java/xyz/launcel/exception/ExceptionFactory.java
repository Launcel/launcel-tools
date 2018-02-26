package xyz.launcel.exception;

/**
 * @author Launcel
 */
public interface ExceptionFactory {

    static void create(String message) {
        throw new ProfessionException(ExceptionHelp.getMessage(message));
    }

    static void error(String message) {
        throw new SystemError("SYSTEM ERROR :" + ExceptionHelp.getMessage(message));
    }

    static void error(String msg, String msgInfo) {
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        throw new SystemError(sb);
    }

    static void create(String msg, String msgInfo) {
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        throw new ProfessionException(sb);
    }
}
