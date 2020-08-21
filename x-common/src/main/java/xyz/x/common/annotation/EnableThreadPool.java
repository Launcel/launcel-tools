package xyz.x.common.annotation;

import org.springframework.context.annotation.Import;
import xyz.x.common.autoconfigure.ThreadPoolConfiguration;

@Import(ThreadPoolConfiguration.class)
public @interface EnableThreadPool
{}
