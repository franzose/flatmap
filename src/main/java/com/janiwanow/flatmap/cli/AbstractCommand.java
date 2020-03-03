package com.janiwanow.flatmap.cli;

import java.lang.reflect.Type;

/**
 * Basic implementation of the console command.
 */
public abstract class AbstractCommand implements Command {
    private Application application;

    @Override
    public void execute(String... args) throws CommandNotFoundException {
        this.application.run(args);
    }

    @Override
    public void execute(Type type) throws CommandNotFoundException {
        this.application.run(type);
    }

    @Override
    public void setApplication(Application application) {
        this.application = application;
    }
}
