package com.janiwanow.flatmap.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

/**
 * Utility to work with files located at the "resources" directory.
 */
public final class ResourceFile {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceFile.class);

    /**
     * Reads entire file at given path to {@link String}.
     *
     * @param cls class for which to search a resource file
     * @param path relative path to a resource file
     * @return file contents
     * @throws FileNotFoundException if case the file at given path does not exist
     * @throws IOException if case of read errors
     */
    public static String readToString(Class<?> cls, String path) throws IOException {
        Objects.requireNonNull(path, "Resource file path must not be null.");

        LOG.info("Try reading a resource file at path \"{}\"...", path);

        var bytes = cls.getResourceAsStream(path).readAllBytes();

        if (bytes == null) {
            throw new FileNotFoundException(String.format(
                "Resource file at path \"%s\" was not found.", path
            ));
        }

        var contents = new String(bytes);

        LOG.info("Resource file contents has been read.");

        return contents;
    }
}
