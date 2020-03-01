package com.janiwanow.flatmap.util;

import java.util.Set;

public final class EnvVariablesMissingException extends RuntimeException {
    public EnvVariablesMissingException(Set<String> names) {
        super("Environment variables " + String.join(", ", names) + " are missing.");
    }
}
