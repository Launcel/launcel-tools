package xyz.launcel.exception;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.RootLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class ExceptionHelp
{

    private ExceptionHelp() { }

    private static Properties props = null;

    public static void initProperties()
    {
        try
        {
            if (Objects.isNull(props))
            {
                var resolver  = new PathMatchingResourcePatternResolver();
                var resources = resolver.getResources("classpath*:/properties/error.properties");
                props = new Properties();
                InputStream       in  = null;
                InputStreamReader inr = null;
                try
                {
                    if (!CollectionUtils.sizeIsEmpty(resources))
                    {
                        for (Resource resource : resources)
                        {
                            in = resource.getInputStream();
                            inr = new InputStreamReader(in, StandardCharsets.UTF_8);
                            props.load(inr);
                        }
                    }
                }
                finally
                {
                    if (inr != null) { inr.close(); }
                    if (in != null) { in.close(); }
                }
                if (RootLogger.isDebug()) { RootLogger.debug("  >>>   错误信息加载完毕！"); }
            }
        }
        catch (IOException e) { throw new SystemException("  >>>   错误信息文件加载失败!"); }
    }

    static Map<String, String> getMessage(String code)
    {
        var map   = new HashMap<String, String>();
        var value = props.getProperty(code);
        if (StringUtils.isNotBlank(value)) { map.put("message", value); }
        else { map.put("message", code); }
        return map;
    }
}
