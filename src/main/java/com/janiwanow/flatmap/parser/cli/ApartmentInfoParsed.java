package com.janiwanow.flatmap.parser.cli;

import com.janiwanow.flatmap.data.ApartmentInfo;
import com.janiwanow.flatmap.event.Event;

/**
 * An event raised when {@link ParseWebsitesCommand} finished parsing and there were some apartments found.
 */
public final class ApartmentInfoParsed implements Event {
    public final ApartmentInfo[] apartments;

    public ApartmentInfoParsed(ApartmentInfo[] apartments) {
        this.apartments = apartments;
    }
}
