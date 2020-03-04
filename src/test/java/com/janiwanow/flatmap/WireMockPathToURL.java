package com.janiwanow.flatmap;

import java.net.URI;
import java.net.URISyntaxException;

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
     * @throws URISyntaxException in case of an improper URL string
     */
    public static URI toAbsoluteURL(String path) throws URISyntaxException {
        var config = wireMockConfig();

        return new URI(String.format(
            "http://%s:%d%s",
            config.bindAddress(),
            config.portNumber(),
            path
        ));
    }
}
