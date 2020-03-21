package com.janiwanow.flatmap.console;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.janiwanow.flatmap.http.DelayRange;
import com.janiwanow.flatmap.internal.console.Command;
import com.janiwanow.flatmap.offer.RelevanceCheckResult;
import com.janiwanow.flatmap.offer.RelevanceChecker;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.janiwanow.flatmap.util.Env.ENV;
import static com.janiwanow.flatmap.util.Executors.randomlyDelayed;
import static java.util.stream.Collectors.*;

@Parameters(commandNames = "check-relevance")
public final class CheckRelevanceCommand implements Command {
    private final BiConsumer<Integer, Consumer<List<URI>>> fetchURLsByChunks;
    private final Function<List<URI>, Integer> markObsolete;
    private final Set<RelevanceChecker> checkers;

    @Parameter(names = "--chunk-size", description = "Number of records to fetch from the database at a time")
    private int chunkSize = 500;

    @Parameter(names = "--delay-min", description = "Minimum delay between HTTP connections")
    private int minDelay = Integer.parseInt(ENV.get("DELAY_MIN", String.valueOf(DelayRange.MIN_DEFAULT)));

    @Parameter(names = "--delay-max", description = "Maximum delay between HTTP connections")
    private int maxDelay = Integer.parseInt(ENV.get("DELAY_MAX", String.valueOf(DelayRange.MAX_DEFAULT)));

    public CheckRelevanceCommand(
        BiConsumer<Integer, Consumer<List<URI>>> fetchURLsByChunks,
        Function<List<URI>, Integer> markObsolete,
        Set<RelevanceChecker> checkers
    ) {
        Objects.requireNonNull(fetchURLsByChunks, "Query must not be null.");
        Objects.requireNonNull(markObsolete, "Query must not be null.");
        Objects.requireNonNull(checkers, "Checkers must not be null.");

        this.fetchURLsByChunks = fetchURLsByChunks;
        this.markObsolete = markObsolete;
        this.checkers = checkers;
    }

    @Override
    public void execute() {
        fetchURLsByChunks.accept(chunkSize, urls -> markObsolete.apply(collect(check(urls))));
    }

    /**
     * Checks the given URLs for relevance.
     *
     * @param urls the URLs to check
     * @return set of futures
     */
    private Set<CompletableFuture<RelevanceCheckResult>> check(List<URI> urls) {
        Set<CompletableFuture<RelevanceCheckResult>> futures = new HashSet<>();

        for (var url : urls) {
            for (var checker : checkers) {
                if (checker.supports(url)) {
                    futures.add(CompletableFuture.supplyAsync(
                        () -> checker.check(url),
                        randomlyDelayed(new DelayRange(minDelay, maxDelay))
                    ));
                }
            }
        }

        return futures;
    }

    /**
     * Collects the given futures to a list of the obsolete URLs.
     *
     * @param futures the futures checking property offers for relevance
     * @return a list of obsolete property offer URLs
     */
    private static List<URI> collect(Set<CompletableFuture<RelevanceCheckResult>> futures) {
        return futures
            .stream()
            .map(CompletableFuture::join)
            .filter(result -> result.isObsolete)
            .map(result -> result.url)
            .collect(toList());
    }
}
