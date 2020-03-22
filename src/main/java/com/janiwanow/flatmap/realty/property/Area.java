package com.janiwanow.flatmap.realty.property;

import java.util.Objects;

/**
 * Data class representing property area details including number of rooms.
 */
public final class Area {
    public final double total;
    public final double living;
    public final double kitchen;
    public final int rooms;

    /**
     * @param total Total apartment area, Sq m.
     * @param living Living area, Sq m.
     * @param kitchen Kitchen area, Sq m.
     * @param rooms Number of rooms in the property
     */
    public Area(double total, double living, double kitchen, int rooms) {
        // 0.0 is assumed as lack of data
        // i.e. when a parser didn't manage to get
        // the information from an offer page
        this.total = Math.max(0.0, total);
        this.living = Math.max(0.0, living);
        this.kitchen = Math.max(0.0, kitchen);
        this.rooms = Math.max(1, rooms);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return Double.compare(area.total, total) == 0 &&
            Double.compare(area.living, living) == 0 &&
            Double.compare(area.kitchen, kitchen) == 0 &&
            rooms == area.rooms;
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, living, kitchen, rooms);
    }
}
