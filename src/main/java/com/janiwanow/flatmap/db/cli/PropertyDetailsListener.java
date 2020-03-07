package com.janiwanow.flatmap.db.cli;

import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.db.ConnectionFactory;
import com.janiwanow.flatmap.parser.cli.PropertyDetailsParsed;
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
    private final ConnectionFactory factory;

    public PropertyDetailsListener(ConnectionFactory factory) {
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

        // TODO: exception handling
        try (
            var conn = factory.getConnection();
            var stmt = conn.prepareStatement(buildInsertQuery(event.items.length))
        ) {
            prepareInsertStatement(stmt, event.items);

            LOG.info("{} properties have been inserted.", stmt.executeUpdate());
        }
    }

    /**
     * Builds the INSERT query for property details.
     *
     * @param itemsNumber to generate a valid number of placeholders
     * @return an INSERT query
     */
    private String buildInsertQuery(int itemsNumber) {
        var queryBuilder = new StringBuilder();
        queryBuilder
            .append("INSERT INTO property (")
            .append("id, offer_url, address, total_area, living_space, kitchen_area, rooms, price_amount, price_currency")
            .append(") VALUES");

        queryBuilder.append("(?, ?, ?, ?, ?, ?, ?, ?, ?),".repeat(itemsNumber));

        return queryBuilder.deleteCharAt(queryBuilder.length() - 1).toString();
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
            stmt.setDouble(idx * cols + 4, propertyDetails.space.total);
            stmt.setDouble(idx * cols + 5, propertyDetails.space.living);
            stmt.setDouble(idx * cols + 6, propertyDetails.space.kitchen);
            stmt.setInt(idx * cols + 7, propertyDetails.space.rooms);
            stmt.setDouble(idx * cols + 8, propertyDetails.price.amount);
            stmt.setString(idx * cols + 9, propertyDetails.price.currency.getCurrencyCode());
        }
    }
}
