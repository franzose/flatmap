package com.janiwanow.flatmap.parser.url_extractor;

import java.net.URI;

class Origin {
    static String getOrigin(URI url) {
        return String.format("%s://%s", url.getScheme(), url.getHost());
    }
}
