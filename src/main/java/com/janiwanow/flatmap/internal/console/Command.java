package com.janiwanow.flatmap.internal.console;

/**
 * A common interface for all console commands.
 */
@FunctionalInterface
public interface Command {
    /**
     * Executes this command.
     */
    void execute();
}
