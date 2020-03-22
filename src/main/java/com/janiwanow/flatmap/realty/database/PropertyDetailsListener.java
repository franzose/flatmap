package com.janiwanow.flatmap.realty.database;

import com.janiwanow.flatmap.console.event.PropertyDetailsParsed;
import com.janiwanow.flatmap.internal.sql.DbConnectionFactory;
import com.janiwanow.flatmap.realty.property.PropertyDetails;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Event listener which react on events related to property details and deals with database.
 */
public class PropertyDetailsListener {
    private static final Logger LOG = LoggerFactory.getLogger(PropertyDetailsListener.class);
    private final DbConnectionFactory factory;

    public PropertyDetailsListener(DbConnectionFactory factory) {
        this.factory = factory;
    }

    /**
     * Saves property details to database.
     *
     * @param event containing parsed property details
     * @throws SQLException
     */
    @Subscribe
    public void save(PropertyDetailsParsed event) throws SQLException {
        LOG.info("Start insertion of property details into the database...");

        try (
            var conn = factory.getConnection();
            var stmt = conn.prepareStatement(buildInsertQuery(event.items.length))
        ) {
            prepareInsertStatement(stmt, event.items);

            LOG.info("{} properties have been inserted.", stmt.executeUpdate());
        } catch (SQLException e) {
            // 23514 is a check constraint failure
            if (!e.getSQLState().equals("23514")) {
                throw e;
            }
        }
    }

    /**
     * Builds the INSERT query for property details.
     *
     * @param itemsNumber to generate a valid number of placeholders
     * @return an INSERT query
     */
    private static String buildInsertQuery(int itemsNumber) {
        var queryBuilder = new StringBuilder()
            .append("INSERT INTO property (")
            .append("id, offer_url, address, ")
            .append("total_area, living_space, kitchen_area, rooms, ")
            .append("price_amount, price_currency, last_checked_at")
            .append(") VALUES ")
            .append("(?, ?, ?, ?, ?, ?, ?, ?, ?, NOW()::timestamp),".repeat(itemsNumber));

        queryBuilder
            .deleteCharAt(queryBuilder.length() - 1)
            // By default, parsers do not differentiate new properties from
            // the already visited ones, so we just ignore duplicates
            // which may get in this listener because it is simpler
            // to check data freshness/obsolescence in a separate
            // command  rather than to make a workaround here
            .append(" ON CONFLICT DO NOTHING");

        return queryBuilder.toString();
    }

    /**
     * Provides the given {@link PreparedStatement} with data of the property details.
     *
     * @param stmt SQL statement which needs to be fed with data
     * @param items property details
     * @throws SQLException
     */
    private static void prepareInsertStatement(PreparedStatement stmt, PropertyDetails[] items) throws SQLException {
        var cols = 9;

        for (int idx = 0; idx < items.length; idx++) {
            var propertyDetails = items[idx];

            stmt.setObject(idx * cols + 1, UUID.randomUUID());
            stmt.setString(idx * cols + 2, propertyDetails.url.toString());
            stmt.setString(idx * cols + 3, propertyDetails.address);
            stmt.setDouble(idx * cols + 4, propertyDetails.area.total);
            stmt.setDouble(idx * cols + 5, propertyDetails.area.living);
            stmt.setDouble(idx * cols + 6, propertyDetails.area.kitchen);
            stmt.setInt(idx * cols + 7, propertyDetails.area.rooms);
            stmt.setDouble(idx * cols + 8, propertyDetails.price.amount);
            stmt.setString(idx * cols + 9, propertyDetails.price.currency.getCurrencyCode());
        }
    }
}
