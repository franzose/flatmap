package com.janiwanow.flatmap.data;

/**
 * Data class representing apartment area details including number of rooms.
 */
public final class Space {
    public final double total;
    public final double living;
    public final double kitchen;
    public final int rooms;

    /**
     * @param total Total apartment area, Sq m.
     * @param living Living space, Sq m.
     * @param kitchen Kitchen area, Sq m.
     * @param rooms Number of rooms in the apartment
     */
    public Space(double total, double living, double kitchen, int rooms) {
        this.total = total;
        this.living = living;
        this.kitchen = kitchen;
        this.rooms = rooms;
    }
}
