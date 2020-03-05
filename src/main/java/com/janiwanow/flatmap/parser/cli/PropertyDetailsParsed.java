package com.janiwanow.flatmap.parser.cli;

import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.event.Event;

/**
 * An event raised when {@link ParseWebsitesCommand} finished parsing and there were some apartments found.
 */
public final class PropertyDetailsParsed implements Event {
    public final PropertyDetails[] apartments;

    public PropertyDetailsParsed(PropertyDetails[] apartments) {
        this.apartments = apartments;
    }
}
