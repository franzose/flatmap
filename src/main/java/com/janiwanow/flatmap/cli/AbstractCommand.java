package com.janiwanow.flatmap.cli;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Basic implementation of the console command.
 */
public abstract class AbstractCommand implements Command {
    private Application application;

    @Override
    public void execute(String... args) throws CommandNotFoundException {
        Objects.requireNonNull(args, "CLI arguments must not be null.");
        this.application.run(args);
    }

    @Override
    public void execute(Type type) throws CommandNotFoundException {
        Objects.requireNonNull(type, "Command type must not be null.");
        this.application.run(type);
    }

    @Override
    public void setApplication(Application application) {
        Objects.requireNonNull(application, "Console application must not be null.");

        this.application = application;
    }
}
