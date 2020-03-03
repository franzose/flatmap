package com.janiwanow.flatmap.event;

/**
 * Event dispatcher.
 */
@FunctionalInterface
public interface EventDispatcher {
    /**
     * Notifies subscribers of the given events.
     *
     * @param events the events to dispatch
     */
    void dispatch(Event... events);
}
