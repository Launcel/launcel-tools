package xyz.launcel.exception;

import lombok.var;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.lang.StringUtils;
import xyz.launcel.log.RootLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ExceptionHelp
{

    private ExceptionHelp() { }

    private final static Properties props = new Properties();

    public static void initProperties()
    {
        try
        {
            synchronized (props)
            {
                if (props.isEmpty())
                {
                    ResourcePatternResolver resolver  = new PathMatchingResourcePatternResolver();
                    Resource[]              resources = resolver.getResources("classpath*:/properties/error.properties");
                    InputStream             in        = null;
                    InputStreamReader       inr       = null;
                    try
                    {
                        if (!CollectionUtils.sizeIsEmpty(resources))
                        {
                            for (var resource : resources)
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
        }
        catch (IOException e) { throw new SystemException("  >>>   错误信息文件加载失败!"); }
    }

    static Map<String, String> getMessage(String code)
    {
        Map<String, String> map   = new HashMap<>();
        String              value = props.getProperty(code);
        if (StringUtils.isNotBlank(value))
        {
            map.put("message", value);
        }
        else { map.put("message", code); }
        return map;
    }
}
