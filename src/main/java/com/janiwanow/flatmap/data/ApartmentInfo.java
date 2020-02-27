package com.janiwanow.flatmap.data;

import java.net.URL;

public final class ApartmentInfo {
    public final URL url;
    public final String address;
    public final Space space;
    public final Price price;

    public ApartmentInfo(URL url, String address, Space space, Price price) {
        this.url = url;
        this.address = address;
        this.price = price;
        this.space = space;
    }
}
