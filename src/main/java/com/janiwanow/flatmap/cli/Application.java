package com.janiwanow.flatmap.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.MissingCommandException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Console application.
 */
public final class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);
    private JCommander commander;

    /**
     * @param commands a set of commands which this application is able to handle
     */
    public Application(Set<Command> commands) {
        var builder = JCommander.newBuilder();
        commands.forEach(builder::addCommand);
        commander = builder.build();
    }

    /**
     * Tries to parse CLI arguments and execute the command if it was found.
     *
     * @param args CLI arguments
     */
    public void run(String... args) {
        try {
            commander.parse(args);

            // we don't check for NULL here because JCommander
            // will throw MissingCommandException if it cannot
            // find an appropriate command, so in this case
            // this statement is unreachable anyway
            getCommand().execute();
        } catch (MissingCommandException e) {
            LOG.warn("Console command \"{}\" not found.", e.getUnknownCommand(), e);
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
