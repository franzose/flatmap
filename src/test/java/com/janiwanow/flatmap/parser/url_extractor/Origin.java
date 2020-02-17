package com.janiwanow.flatmap.parser.url_extractor;

import java.net.URL;

class Origin {
    static String getOrigin(URL url) {
        return String.format("%s://%s", url.getProtocol(), url.getHost());
    }
}
