package xyz.launcel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import xyz.launcel.lang.Json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class ExceptionHelp {

    private ExceptionHelp() {
    }


    private static Properties props = null;

    public static void initProperties() {
        Logger log = LoggerFactory.getLogger(ExceptionHelp.class);
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
                if (log.isDebugEnabled())
                    log.debug("  >>>   错误信息加载完毕！");
            }
        } catch (IOException e) {
            throw new ProfessionException("  >>>   错误信息文件加载失败!");
        }
    }

    protected static Map<String, String> getMessage(String code) {
        Map<String, String> map = new HashMap<>();
        try {
            String value = props.getProperty(code);
            map.put("message", value);
        } catch (Exception x) {
            map.clear();
            map.put("message", "不存在的错误信息!");
//            throw new ProfessionException(Json.toJson(map).replaceAll("\\{", "[").replaceAll("}", "]"));
//            throw new ProfessionException(Json.toJson(map));
        }
//        return Json.toJson(map).replaceAll("\\{", "[").replaceAll("}", "]");
        return map;
    }
}
