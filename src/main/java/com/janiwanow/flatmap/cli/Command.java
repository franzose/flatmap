package com.janiwanow.flatmap.cli;

import java.lang.reflect.Type;

/**
 * A common interface for all console commands.
 */
public interface Command {
    /**
     * Executes this command.
     */
    void execute();

    /**
     * Runs another command.
     *
     * <p>Example:
     * <pre>
     * <code>
     *     public void execute() {
     *         //
     *         // some code...
     *         //
     *
     *         execute("foo", "--bar", "--qux");
     *
     *         // continue doing something...
     *     }
     * </code>
     * </pre>
     *
     * <p>Notice that "foo" must be registered in the {@link Application} first.
     *
     * @param args CLI arguments
     * @throws CommandNotFoundException if no command of the given name was found
     */
    void execute(String... args) throws CommandNotFoundException;

    /**
     * Runs another command.
     *
     * <p>Example:
     * <pre>
     * <code>
     *     public void execute() {
     *         //
     *         // some code...
     *         //
     *
     *         execute(FooBarCommand.class);
     *
     *         // continue doing something...
     *     }
     * </code>
     * </pre>
     *
     * <p>Notice that FooBarCommand must be registered in the {@link Application} first.
     *
     * @param type command type
     * @throws CommandNotFoundException if no command of the given type was found
     */
    void execute(Type type) throws CommandNotFoundException;

    /**
     * Sets a console application instance to this command.
     *
     * <p>This method is guaranteed to be called from within the application.
     *
     * @param application an instance of the console application
     */
    void setApplication(Application application);
}
