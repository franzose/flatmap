package com.janiwanow.flatmap.data;

import java.net.URI;
import java.util.Objects;

/**
 * Data class representing basic property details.
 */
public final class PropertyDetails {
    public final URI url;
    public final String address;
    public final Area area;
    public final Price price;

    /**
     * @param url URL of the original offer page from which the information was taken
     * @param address Property address
     * @param area Property area details
     * @param price Rental or purchase price
     */
    public PropertyDetails(URI url, String address, Area area, Price price) {
        Objects.requireNonNull(url, "URL must not be null.");
        Objects.requireNonNull(address, "Address must not be null.");
        Objects.requireNonNull(area, "Area must not be null.");
        Objects.requireNonNull(price, "Price must not be null.");

        this.url = url;
        this.address = address;
        this.price = price;
        this.area = area;
    }
}
