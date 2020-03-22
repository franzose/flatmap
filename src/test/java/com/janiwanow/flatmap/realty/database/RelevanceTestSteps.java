package com.janiwanow.flatmap.realty.database;

import com.janiwanow.flatmap.internal.sql.DbConnectionFactory;
import com.janiwanow.flatmap.internal.sql.TestDbConnectionFactory;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

public final class RelevanceTestSteps {
    private List<URI> urls = new ArrayList<>();

    public List<URI> getUrls() {
        var result = new ArrayList<>(urls);
        urls.clear();

        return result;
    }

    @Given("the database contains property details from the following URLs")
    public void setUpDatabase(DataTable data) throws SQLException {
        urls = getUrlsFromDataTable(data);

        insertRows(TestDbConnectionFactory.INSTANCE, urls);
    }

    private static List<URI> getUrlsFromDataTable(DataTable data) {
        return data.asList()
            .stream()
            .map(url -> {
                try {
                    return new URI(url);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            })
            .collect(toList());
    }

    private static void insertRows(DbConnectionFactory db, List<URI> urls) throws SQLException {
        try (
            var conn = db.getConnection();
        ) {
            var queryBuilder = new StringBuilder();
            var query = queryBuilder
                .append("INSERT INTO property (")
                .append("id, offer_url, address, ")
                .append("total_area, living_space, kitchen_area, rooms, ")
                .append("price_amount, price_currency, last_checked_at) ")
                .append("VALUES ")
                .append("(?, ?, 'Foo Bar', 10, 5, 5, 1, 1000000, 'RUB', NOW()::timestamp),".repeat(urls.size()))
                .deleteCharAt(queryBuilder.length() - 1)
                .toString();

            try (var stmt = conn.prepareStatement(query)) {
                var cols = 2;

                for (var idx = 0; idx < urls.size(); idx++) {
                    stmt.setObject(idx * cols + 1, UUID.randomUUID());
                    stmt.setString(idx * cols + 2, urls.get(idx).toString());
                }

                stmt.executeUpdate();
            }
        }
    }
}
