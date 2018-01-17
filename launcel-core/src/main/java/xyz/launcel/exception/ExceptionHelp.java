package xyz.launcel.exception;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import xyz.launcel.lang.Json;
import xyz.launcel.log.BaseLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class ExceptionHelp extends BaseLogger {

    private static Properties props = null;

    public static void initProperties() {
        try {
            if (Objects.isNull(props)) {
                PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resolver.getResources("classpath*:/prop/error.properties");
                props = new Properties();
                InputStream in = null;
                InputStreamReader inr = null;
                try {
                    for (Resource resource : resources) {
                        in = resource.getInputStream();
                        inr = new InputStreamReader(in, "UTF-8");
                        props.load(inr);
                    }
                } finally {
                    if (inr != null) inr.close();
                    if (in != null) in.close();
                }
                if (debugEnabled())
                    Debug("错误信息加载完毕！");
            }
        } catch (IOException e) {
            throw new SystemError("错误信息文件加载失败!");
        }
    }

    protected static String getMessage(String code) {
        Map<String, String> map = new HashMap<>();
        try {
            String value = props.getProperty(code);
            map.put("code", code);
            map.put("message", value);
        } catch (Exception x) {
            map.clear();
            map.put("code", "MESSAGE_ERROR_001");
            map.put("message", "不存在的错误信息!");
            throw new ProfessionException(Json.toJson(map));
        }
        return Json.toJson(map);
    }
}
