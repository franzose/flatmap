package com.janiwanow.flatmap.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;

import java.util.Objects;
import java.util.Set;

/**
 * Console application.
 */
public final class Application {
    private JCommander commander;

    /**
     * @param commands a set of commands which this application is able to handle
     */
    public Application(Set<Command> commands) {
        Objects.requireNonNull(commands, "Console commands must not be null.");

        var builder = JCommander.newBuilder();
        commands.forEach(builder::addCommand);
        commander = builder.build();
    }

    /**
     * Tries to parse CLI arguments and execute the command if it was found.
     *
     * @param args CLI arguments
     */
    public void run(String... args) throws CommandNotFoundException {
        Objects.requireNonNull(args, "CLI arguments must not be null.");

        try {
            commander.parse(args);

            // we don't check for NULL here because JCommander
            // will throw MissingCommandException if it cannot
            // find an appropriate command, so in this case
            // this statement is unreachable anyway
            getCommand().execute();
        } catch (MissingCommandException e) {
            throw new CommandNotFoundException(e);
        }
    }

    /**
     * Retrieves the command object from the underlying {@link JCommander}.
     *
     * @return command object if the CLI arguments were parsed successfully
     */
    private Command getCommand() {
        var commandName = commander.getParsedCommand();

        // Cast is safe here because the application
        // accepts only Command instances anyway
        return (Command) commander
            .findCommandByAlias(commandName)
            .getObjects()
            .get(0);
    }
}
