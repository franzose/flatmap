package com.janiwanow.flatmap.internal.eventbus;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

/**
 * Event dispatcher based on Greenrobot's {@link EventBus}.
 */
public class GreenRobotEventDispatcher implements EventDispatcher {
    private final EventBus eventBus;

    public GreenRobotEventDispatcher(EventBus eventBus) {
        Objects.requireNonNull(eventBus, "Event bus must not be null.");
        this.eventBus = eventBus;
    }

    @Override
    public void dispatch(Event... events) {
        Objects.requireNonNull(events, "Events must not be null.");

        for (var event : events) {
            eventBus.post(event);
        }
    }
}
