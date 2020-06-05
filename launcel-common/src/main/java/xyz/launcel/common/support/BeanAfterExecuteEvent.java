package xyz.launcel.common.support;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.lang.NonNull;
import xyz.launcel.common.exception.ExceptionFactory;
import xyz.launcel.common.utils.CollectionUtils;
import xyz.launcel.log.Log;

import java.util.Collections;
import java.util.List;

public interface BeanAfterExecuteEvent extends ApplicationListener<ApplicationContextEvent>
{
    @Override
    default void onApplicationEvent(@NonNull ApplicationContextEvent event)
    {
        List<String> beanList = getDependBean();
        try
        {
            if (CollectionUtils.isEmpty(beanList))
            {
                afterExecute();
                return;
            }
            for (String s : beanList)
            {
                if (!event.getApplicationContext().containsBean(s))
                {
                    ExceptionFactory.create(s + " bean is not found!!!");
                }
            }
            afterExecute();
        }
        catch (Exception x)
        {
            Log.error("error =", x);
        }
    }

    default List<String> getDependBean()
    {
        return Collections.emptyList();
    }

    void afterExecute() throws Exception;
}
