package xyz.launcel.exception;

import xyz.launcel.lang.Json;
import xyz.launcel.log.RootLogger;

import java.util.Map;

/**
 * @author Launcel
 */
public interface ExceptionFactory {
    
    
    static void create(String message) {
        Map<String, String> map = ExceptionHelp.getMessage(message);
        RootLogger.error("\t{}", Json.toJson(map).replaceAll("\\{", "[").replaceAll("}", "]"));
        throw new ProfessionException(map.values().toString());
    }
    
    static void error(String message) {
        Map<String, String> map = ExceptionHelp.getMessage(message);
        RootLogger.error("\t{}", Json.toJson(map).replaceAll("\\{", "[").replaceAll("}", "]"));
        throw new SystemError(map.values().toString());
    }
    
    static void error(String msg, String msgInfo) {
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        RootLogger.error(sb);
        throw new SystemError(msgInfo);
    }
    
    static void create(String msg, String msgInfo) {
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        RootLogger.error(sb);
        throw new ProfessionException(msgInfo);
    }
}
