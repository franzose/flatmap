package com.janiwanow.flatmap.cli;

import com.beust.jcommander.MissingCommandException;

import java.lang.reflect.Type;

public class CommandNotFoundException extends Exception {
    public CommandNotFoundException(MissingCommandException cause) {
        super(String.format("Console command \"%s\" not found.", cause.getUnknownCommand()), cause);
    }

    public CommandNotFoundException(Type type) {
        super(String.format("Console command of type \"%s\" not found.", type.getTypeName()));
    }
}
