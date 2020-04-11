package xyz.launcel.util;

import com.google.gson.JsonObject;
import lombok.var;
import org.apache.commons.io.IOUtils;
import org.springframework.web.util.WebUtils;
import xyz.launcel.annotation.ToolsClass;
import xyz.launcel.utils.CollectionUtils;
import xyz.launcel.utils.Json;
import xyz.launcel.utils.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by launcel on 2018/8/3.
 */
@ToolsClass
public class WebObjectUtil
{
    public static JsonObject parseParamFromRequest(HttpServletRequest request)
    {
        String paramInput   = null;
        var    parameterMap = WebUtils.getParametersStartingWith(request, null);

        if (CollectionUtils.isNotEmpty(parameterMap)) {
            paramInput = Json.toString(parameterMap);
        }

        if (StringUtils.isBlank(paramInput)) {
            try {
                paramInput = IOUtils.toString(request.getInputStream(), "UTF-8");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JsonObject().getAsJsonObject(paramInput);
    }
}
