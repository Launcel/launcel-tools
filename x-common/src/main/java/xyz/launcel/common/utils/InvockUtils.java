package xyz.launcel.common.utils;

import org.springframework.lang.NonNull;
import xyz.launcel.log.Log;

import java.util.function.Function;

public interface InvockUtils
{
    static <P, R> R apply(@NonNull Function<P, R> function, P param)
    {
        try
        {
            return function.apply(param);
        }
        catch (Exception x)
        {
            Log.error("invock method error !!\nparam={}\ncause info is : \n{}", Json.toString(param), x.getCause());
        }
        return null;
    }

}
