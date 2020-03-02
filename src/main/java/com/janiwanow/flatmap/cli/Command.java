package com.janiwanow.flatmap.cli;

/**
 * A common interface for all console commands.
 */
@FunctionalInterface
public interface Command {
    /**
     * Does something useful.
     */
    void execute();
}
