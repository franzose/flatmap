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
public class ResourceFile {
    private static final Logger LOG = LoggerFactory.getLogger(ResourceFile.class);

    /**
     * Reads entire file at given path to {@link String}.
     *
     * @param path relative path to a resource file
     * @return file contents
     * @throws FileNotFoundException if case the file at given path does not exist
     * @throws IOException if case of read errors
     */
    public static String readToString(String path) throws IOException {
        Objects.requireNonNull(path, "Resource file path must not be null.");

        LOG.info("Try reading a resource file at path \"{}\"...", path);

        var resource = ResourceFile.class.getClassLoader().getResource(path);

        if (resource == null) {
            throw new FileNotFoundException(String.format(
                "Resource file at path \"%s\" was not found.", path
            ));
        }

        var file = new File(resource.getFile());
        var contents = new String(Files.readAllBytes(file.toPath()));

        LOG.info("Resource file contents has been read.");

        return contents;
    }
}
