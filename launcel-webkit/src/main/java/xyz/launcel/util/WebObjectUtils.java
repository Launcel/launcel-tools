package xyz.launcel.util;

import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.springframework.web.util.WebUtils;
import xyz.launcel.json.Json;
import xyz.launcel.lang.CollectionUtils;
import xyz.launcel.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by launcel on 2018/8/3.
 */
public class WebObjectUtils
{
    public static JsonObject parseParamFromRequest(HttpServletRequest request)
    {
        String paramInput   = null;
        var    parameterMap = WebUtils.getParametersStartingWith(request, null);

        if (CollectionUtils.isNotEmpty(parameterMap))
        {
            paramInput = Json.toString(parameterMap);
        }

        if (StringUtils.isBlank(paramInput))
        {
            try
            {
                paramInput = IOUtils.toString(request.getInputStream(), "UTF-8");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return new JsonObject().getAsJsonObject(paramInput);
    }
}
