package com.janiwanow.flatmap.offer.db;

import com.janiwanow.flatmap.db.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class FetchURLsByChunks implements BiConsumer<Integer, Consumer<List<URI>>> {
    private static final Logger LOG = LoggerFactory.getLogger(FetchURLsByChunks.class);
    private final static String COUNT_QUERY = "SELECT COUNT(*) FROM property";
    private final static String FIRST_QUERY =
        "SELECT offer_url FROM property " +
            "WHERE invalidated_at IS NULL " +
            "ORDER BY offer_url DESC LIMIT ?";

    private final static String NEXT_QUERY =
        "SELECT offer_url FROM property " +
            "WHERE invalidated_at IS NULL " +
            "AND offer_url < ? " +
            "ORDER BY offer_url DESC LIMIT ?";

    private final ConnectionFactory db;
    private Connection connection;
    private int chunkSize;
    private String lastURL;

    public FetchURLsByChunks(ConnectionFactory db) {
        Objects.requireNonNull(db, "DB connection factory must not be null.");
        this.db = db;
    }

    @Override
    public void accept(Integer chunkSize, Consumer<List<URI>> consumer) {
        Objects.requireNonNull(chunkSize, "Chunk size must not be null.");
        Objects.requireNonNull(consumer, "URLs consumer must not be null.");

        LOG.info("Start fetching URLs from the database. Chunk size: {}", chunkSize);

        try (var connection = db.getConnection()) {
            connection.setAutoCommit(false);

            this.connection = connection;
            this.chunkSize = chunkSize;

            long numberOfQueries = countNumberOfQueries();
            LOG.info("SQL queries to execute: {}", numberOfQueries);

            for (long idx = 0; idx <= numberOfQueries; idx++) {
                var urls = fetchNextChunk();

                if (!urls.isEmpty()) {
                    consumer.accept(urls);
                }
            }
        } catch (SQLException e) {
            LOG.error("Could not fetch URLs by chunks. Chunk size was: {}", chunkSize, e);
            throw new RuntimeException(e);
        }
    }

    long countNumberOfQueries() throws SQLException {
        try (var result = connection.createStatement().executeQuery(COUNT_QUERY)) {
            var total = result.next() ? result.getLong(1) : 0;
            return Math.round(Math.ceil(total / (double) chunkSize));
        }
    }

    List<URI> fetchNextChunk() throws SQLException {
        List<URI> urls = new ArrayList<>();

        try (
            var stmt = lastURL == null ? getStatement() : getStatement(lastURL);
            var result = stmt.executeQuery()
        ) {
            while (result.next()) {
                lastURL = result.getString(1);

                try {
                    urls.add(new URI(lastURL));
                } catch (URISyntaxException e) {
                    LOG.error("Unexpected URI syntax: '{}'. Examine the database.", lastURL, e);
                    throw new RuntimeException(e);
                }
            }
        }

        return urls;
    }

    private PreparedStatement getStatement() throws SQLException {
        var stmt = connection.prepareStatement(FIRST_QUERY);
        stmt.setFetchSize(chunkSize);
        stmt.setInt(1, chunkSize);

        return stmt;
    }

    private PreparedStatement getStatement(String url) throws SQLException {
        var stmt = connection.prepareStatement(NEXT_QUERY);
        stmt.setFetchSize(chunkSize);
        stmt.setString(1, url);
        stmt.setInt(2, chunkSize);

        return stmt;
    }
}
