package com.janiwanow.flatmap.data;

public final class Space {
    public final double total;
    public final double living;
    public final double kitchen;
    public final int rooms;

    public Space(double total, double living, double kitchen, int rooms) {
        this.total = total;
        this.living = living;
        this.kitchen = kitchen;
        this.rooms = rooms;
    }
}
