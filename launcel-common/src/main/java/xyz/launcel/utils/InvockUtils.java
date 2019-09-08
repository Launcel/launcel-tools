package xyz.launcel.utils;

import org.springframework.lang.NonNull;
import xyz.launcel.log.RootLogger;

import java.util.function.Function;

public interface InvockUtils
{
    static <P, R> R execute(@NonNull Function<P, R> function, P param)
    {

        try
        {
            return function.apply(param);
        }
        catch (Exception x)
        {
            RootLogger.error("invock method error !!\nparam={}\ncause info is : \n{}", Json.toString(param), x.getCause());
        }
        return null;
    }


}
