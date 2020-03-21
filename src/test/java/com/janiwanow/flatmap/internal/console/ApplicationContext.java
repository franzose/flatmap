package com.janiwanow.flatmap.internal.console;

import java.util.Set;

public final class ApplicationContext {
    public Application application;

    public void setUpApplication(Command... command) {
        application = new Application(Set.of(command));
    }
}
