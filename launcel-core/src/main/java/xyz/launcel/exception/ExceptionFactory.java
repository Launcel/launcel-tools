package xyz.launcel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.launcel.json.Json;

import java.util.Map;

/**
 * @author Launcel
 */
public interface ExceptionFactory {
    
    Logger log = LoggerFactory.getLogger("root");
    
    static void create(String message) {
        Map<String, String> map = ExceptionHelp.getMessage(message);
        log.error("\t{}", Json.toJson(map).replaceAll("\\{", "[").replaceAll("}", "]"));
        throw new ProfessionException(map.values().toString());
    }
    
    static void error(String message) {
        Map<String, String> map = ExceptionHelp.getMessage(message);
        log.error("\t{}", Json.toJson(map).replaceAll("\\{", "[").replaceAll("}", "]"));
        throw new SystemError(map.values().toString());
    }
    
    static void error(String msg, String msgInfo) {
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        log.error(sb);
        throw new SystemError(msgInfo);
    }
    
    static void create(String msg, String msgInfo) {
        String sb = "\t[" + msg + " : " + msgInfo + "]";
        log.error(sb);
        throw new ProfessionException(msgInfo);
    }
}
