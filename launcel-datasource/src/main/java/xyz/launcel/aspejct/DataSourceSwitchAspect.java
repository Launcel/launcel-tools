package xyz.launcel.aspejct;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by launcel on 2018/8/29.
 */
@Slf4j
@Aspect
@Order(-100)
@Component
public class DataSourceSwitchAspect
{}
