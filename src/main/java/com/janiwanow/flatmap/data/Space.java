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
        // 0.0 is assumed as lack of data
        // i.e. when a parser didn't manage to get
        // the information from an offer page
        this.total = Math.max(0.0, total);
        this.living = Math.max(0.0, living);
        this.kitchen = Math.max(0.0, kitchen);
        this.rooms = Math.max(1, rooms);
    }
}
