package xyz.launcel.common.support;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.lang.NonNull;
import xyz.launcel.common.utils.CollectionUtils;
import xyz.launcel.log.Log;

import java.util.Collections;
import java.util.List;

public abstract class BeanAfterExecuteEvent implements ApplicationListener<ApplicationContextEvent>
{
    @Override
    public void onApplicationEvent(@NonNull ApplicationContextEvent event)
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
                    return;
                }
            }
            afterExecute();
        }
        catch (Exception x)
        {
            Log.error("error =", x);
        }
    }

    protected List<String> getDependBean()
    {
        return Collections.emptyList();
    }

    public abstract void afterExecute() throws Exception;
}
