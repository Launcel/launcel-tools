package xyz.launcel.lang;

import org.springframework.lang.NonNull;
import xyz.launcel.json.Json;
import xyz.launcel.log.RootLogger;

import java.util.function.Function;

public interface RpcInvockUtils
{
    @NonNull
    static <T> T invock(@NonNull final Function<Void, T> function)
    {
        return function.apply(null);
    }

    @NonNull
    static <T> T invock(final Function<Void, T> function, @NonNull Object param)
    {
        T t = null;
        try
        {
            t = invock(function);
        }
        catch (Exception x)
        {
            RootLogger.error("invock method error !!\nparam={}\ncause info is : \n{}", Json.toString(param), x.getCause());
        }
        return t;
    }
}
