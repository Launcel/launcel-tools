package xyz.x.common.support;

import org.springframework.boot.CommandLineRunner;

public interface AfterContextStartUpRunner extends CommandLineRunner
{
    void process();

    @Override
    default void run(String... args)
    {
        process();
    }
}
