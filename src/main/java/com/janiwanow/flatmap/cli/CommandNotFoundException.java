package com.janiwanow.flatmap.cli;

import com.beust.jcommander.MissingCommandException;

public class CommandNotFoundException extends Exception {
    public CommandNotFoundException(MissingCommandException cause) {
        super(String.format("Console command \"%s\" not found.", cause.getUnknownCommand()), cause);
    }
}
