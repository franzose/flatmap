package com.janiwanow.flatmap.data;

import java.net.URL;

public final class ApartmentInfo {
    public final URL url;
    public final String address;
    public final Space space;
    public final double price;

    public ApartmentInfo(URL url, String address, Space space, double price) {
        this.url = url;
        this.address = address;
        this.price = price;
        this.space = space;
    }
}
