package com.janiwanow.flatmap.data;

import java.net.URL;

/**
 * Data class representing basic apartment information details.
 */
public final class ApartmentInfo {
    public final URL url;
    public final String address;
    public final Space space;
    public final Price price;

    /**
     * @param url URL of the original offer page from which the information was taken
     * @param address Apartment address
     * @param space Apartment area details
     * @param price Rental or purchase price
     */
    public ApartmentInfo(URL url, String address, Space space, Price price) {
        this.url = url;
        this.address = address;
        this.price = price;
        this.space = space;
    }
}
