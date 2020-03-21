package com.janiwanow.flatmap.console.event;

import com.janiwanow.flatmap.console.ParseWebsitesCommand;
import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.event.Event;

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
