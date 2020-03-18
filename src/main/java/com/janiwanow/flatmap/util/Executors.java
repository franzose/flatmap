package com.janiwanow.flatmap.util;

import com.janiwanow.flatmap.http.Delay;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * Utility to create custom {@link Executor} instances.
 */
public final class Executors {
    /**
     * Creates a delayed executor intended to use for HTTP requests.
     *
     * @param delay a delay range between HTTP requests
     * @return a delayed executor
     */
    public static Executor randomlyDelayed(Delay delay) {
        return CompletableFuture.delayedExecutor(
            Math.min(delay.max, delay.min + new Random().nextInt(delay.max)),
            TimeUnit.MILLISECONDS
        );
    }
}
