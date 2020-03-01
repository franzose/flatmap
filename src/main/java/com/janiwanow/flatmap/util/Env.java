package com.janiwanow.flatmap.util;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/**
 * Helper utility wrapping {@link Dotenv}.
 */
public final class Env {
    public static final Dotenv ENV = Dotenv.configure().load();

    /**
     * Checks whether all the required environment variables are set.
     *
     * @param requiredVarNames names of the required environment variables
     * @throws EnvVariablesMissingException if any of the required environment variables is missing
     */
    public static void ensureVarsAreSet(Set<String> requiredVarNames) {
        Objects.requireNonNull(requiredVarNames, "Required variable names must not be null.");

        var allEnvVarNames = ENV.entries().stream().map(DotenvEntry::getKey).collect(toSet());
        var missingVarNames = requiredVarNames.stream().dropWhile(allEnvVarNames::contains).collect(toSet());

        if (missingVarNames.size() > 0) {
            throw new EnvVariablesMissingException(missingVarNames);
        }
    }
}
