package com.janiwanow.flatmap.internal.util;

import java.util.Set;

/**
 * An exception thrown by {@link Env} when the given environment variables are missing.
 */
public final class EnvVariablesMissingException extends RuntimeException {
    public EnvVariablesMissingException(Set<String> names) {
        super("Environment variables " + String.join(", ", names) + " are missing.");
    }
}
