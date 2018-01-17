package xyz.launcel.Listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import xyz.launcel.exception.ExceptionHelp;
import xyz.launcel.hook.ApplicationContextHook;

public class CommonListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContextHook.setApplicationContext(event.getApplicationContext());
        ExceptionHelp.initProperties();
    }


}
