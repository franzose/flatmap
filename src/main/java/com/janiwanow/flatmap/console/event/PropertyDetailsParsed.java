package com.janiwanow.flatmap.console.event;

import com.janiwanow.flatmap.console.ParseWebsitesCommand;
import com.janiwanow.flatmap.internal.eventbus.Event;
import com.janiwanow.flatmap.realty.property.PropertyDetails;

/**
 * An event raised when {@link ParseWebsitesCommand} finished parsing
 * and there were some property details found.
 */
public final class PropertyDetailsParsed implements Event {
    public final PropertyDetails[] items;

    public PropertyDetailsParsed(PropertyDetails[] items) {
        this.items = items;
    }
}
