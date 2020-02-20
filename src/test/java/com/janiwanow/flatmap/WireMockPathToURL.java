package com.janiwanow.flatmap;

import java.net.MalformedURLException;
import java.net.URL;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Converter of relative paths to absolute URLs. It intends
 * to be used in conjunction with WireMock connections.
 */
public class WireMockPathToURL {
    /**
     * Converts the given relative path to an absolute URL using WireMock configuration.
     *
     * @param path relative path
     * @return absolute URL
     * @throws MalformedURLException in case of an improper URL string
     */
    public static URL toAbsoluteURL(String path) throws MalformedURLException {
        var config = wireMockConfig();

        return new URL(String.format(
            "http://%s:%d%s",
            config.bindAddress(),
            config.portNumber(),
            path
        ));
    }
}
