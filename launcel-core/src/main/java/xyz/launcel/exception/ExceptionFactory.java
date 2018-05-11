package xyz.launcel.exception;

import xyz.launcel.json.Json;
import xyz.launcel.log.RootLogger;

import java.util.Map;

/**
 * @author Launcel
 */
public interface ExceptionFactory {
    
    
    static void create(String code) {
        Map<String, String> map = ExceptionHelp.getMessage(code);
        RootLogger.ERROR("\t{}", Json.toJson(map).replaceAll("\\{", "[").replaceAll("}", "]"));
        throw new ProfessionException(map.values().toString());
    }
    
    static void error(String code) {
        Map<String, String> map = ExceptionHelp.getMessage(code);
        RootLogger.ERROR("\t{}", Json.toJson(map).replaceAll("\\{", "[").replaceAll("}", "]"));
        throw new SystemError(map.values().toString());
    }
    
    static void error(String code, String msg) {
        String sb = "\t[" + code + " : " + msg + "]";
        RootLogger.ERROR(sb);
        throw new SystemError(msg);
    }
    
    static void create(String code, String msg) {
        String sb = "\t[" + code + " : " + msg + "]";
        RootLogger.ERROR(sb);
        throw new ProfessionException(msg);
    }
}
