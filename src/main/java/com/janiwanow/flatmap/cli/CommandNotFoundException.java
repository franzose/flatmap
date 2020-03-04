package com.janiwanow.flatmap.cli;

import com.beust.jcommander.MissingCommandException;

/**
 * An exception thrown when {@link Application} could not find a command by the given parameters.
 */
public class CommandNotFoundException extends Exception {
    /**
     * @param cause original exception thrown by {@link com.beust.jcommander.JCommander}
     */
    public CommandNotFoundException(MissingCommandException cause) {
        super(String.format("Console command \"%s\" not found.", cause.getUnknownCommand()), cause);
    }
}
