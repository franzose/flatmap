package com.janiwanow.flatmap.offer.db;

import com.janiwanow.flatmap.db.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * A database query which takes a list of URLs, marks them obsolete
 * and returns the number of affected database table rows.
 */
public final class MarkObsolete implements Function<List<URI>, Integer> {
    private static final Logger LOG = LoggerFactory.getLogger(MarkObsolete.class);
    private final ConnectionFactory db;

    public MarkObsolete(ConnectionFactory db) {
        Objects.requireNonNull(db, "DB connection factory must not be null.");
        this.db = db;
    }

    @Override
    public Integer apply(List<URI> urls) {
        Objects.requireNonNull(urls, "URLs must not be null.");

        if (urls.isEmpty()) {
            return 0;
        }

        try (
            var connection = db.getConnection();
            var stmt = connection.prepareStatement(getSQL(urls))
        ) {
            for (var idx = 1; idx <= urls.size(); idx++) {
                stmt.setString(idx, urls.get(idx - 1).toString());
            }

            return stmt.executeUpdate();
        } catch (SQLException e) {
            LOG.error("Could not mark properties obsolete.", e);
            throw new RuntimeException(e);
        }
    }

    private String getSQL(List<URI> urls) {
        var placeholders = "?,".repeat(urls.size());
        placeholders = placeholders.substring(0, placeholders.length() - 1);

        return
            "UPDATE property SET invalidated_at = NOW()::timestamp " +
            "last_checked_at = NOW()::timestamp" +
            "WHERE offer_url IN (" + placeholders + ")";
    }
}
