package xyz.launcel.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import xyz.launcel.log.Log;
import xyz.launcel.utils.CollectionUtils;
import xyz.launcel.utils.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionHelp
{

    private final static Properties props = new Properties();

    public static void initProperties()
    {
        try
        {
            if (props.isEmpty())
            {
                var resolver  = new PathMatchingResourcePatternResolver();
                var resources = resolver.getResources("classpath*:/properties/error.properties");

                if (!CollectionUtils.sizeIsEmpty(resources))
                {
                    for (var resource : resources)
                    {
                        try (var in = resource.getInputStream())
                        {
                            try (var inr = new InputStreamReader(in, StandardCharsets.UTF_8))
                            {
                                props.load(inr);
                            }
                        }
                    }
                }
                if (Log.isDebug())
                {
                    System.out.println("  >>>   错误信息加载完毕！");
                }
            }
        }
        catch (IOException e)
        {
            throw new SystemException("  >>>   错误信息文件加载失败!");
        }
    }

    public static Map<String, String> getMessage(String code)
    {
        var map   = new HashMap<String, String>();
        var value = props.getProperty(code);
        map.put("message", code);
        if (StringUtils.isNotBlank(value))
        {
            map.put("message", value);
        }
        return map;
    }
}
