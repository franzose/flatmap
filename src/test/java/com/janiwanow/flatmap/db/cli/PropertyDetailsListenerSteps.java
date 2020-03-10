package com.janiwanow.flatmap.db.cli;

import com.janiwanow.flatmap.data.Area;
import com.janiwanow.flatmap.data.Price;
import com.janiwanow.flatmap.data.PropertyDetails;
import com.janiwanow.flatmap.db.TestConnectionFactory;
import com.janiwanow.flatmap.parser.cli.PropertyDetailsParsed;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public final class PropertyDetailsListenerSteps {
    private static final TestConnectionFactory DATABASE = TestConnectionFactory.getInstance();
    private PropertyDetails expectedDetails;
    private PropertyDetails actualDetails;
    private PropertyDetailsListener listener = new PropertyDetailsListener(DATABASE);
    private String url;

    @Given("I obtained some property details")
    public void setUpListener(DataTable data) throws URISyntaxException {
        var values = data.asList();

        expectedDetails = new PropertyDetails(
            new URI(values.get(0)),
            values.get(1),
            new Area(
                Integer.parseInt(values.get(2)),
                Integer.parseInt(values.get(3)),
                Integer.parseInt(values.get(4)),
                Integer.parseInt(values.get(5))
            ),
            new Price(
                Currency.getInstance(values.get(7)),
                Double.parseDouble(values.get(6))
            )
        );
    }

    @Given("I have already put the property details obtained from {string}")
    public void setUpPropertyDetails(String url) throws SQLException, URISyntaxException {
        this.url = url;
        insertFromURL(url);
    }

    @When("I put the property details into the database")
    public void insertNewPropertyDetails() throws SQLException {
        listener.save(new PropertyDetailsParsed(new PropertyDetails[]{ expectedDetails }));
    }

    @When("I put property details from the same URL")
    public void insertFromTheSameURL() throws SQLException, URISyntaxException {
        insertFromURL(url);
    }

    private void insertFromURL(String url) throws SQLException, URISyntaxException {
        listener.save(new PropertyDetailsParsed(new PropertyDetails[]{
            new PropertyDetails(
                new URI(url),
                "Южно-Сахалинск, проспект Мира, 121",
                new Area(
                    36,
                    20,
                    9,
                    1
                ),
                Price.inRubles(1_000_000)
            )
        }));
    }

    @And("I query for the property details from {string}")
    public void queryForThePropertyDetails(String url) throws SQLException, URISyntaxException {
        var sql = "SELECT * FROM property WHERE offer_url = ?";

        try (
            var conn = DATABASE.getConnection();
            var stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, url);

            try (var result = stmt.executeQuery()) {
                if (!result.next()) {
                    fail(String.format("No property details found by %s URL", url));
                }

                actualDetails = new PropertyDetails(
                    new URI(result.getString("offer_url")),
                    result.getString("address"),
                    new Area(
                        result.getDouble("total_area"),
                        result.getDouble("living_space"),
                        result.getDouble("kitchen_area"),
                        result.getInt("rooms")
                    ),
                    new Price(
                        Currency.getInstance(result.getString("price_currency")),
                        result.getDouble("price_amount")
                    )
                );
            }
        }
    }

    @Then("I must get those property details")
    public void ensurePropertyDetailsAreValid() {
        assertEquals(expectedDetails, actualDetails);
    }

    @Then("the database must ignore these property details")
    public void ensurePropertyDetailsAreIgnored() throws SQLException {
        var sql = "SELECT COUNT(*) FROM property";

        try (
            var conn = DATABASE.getConnection();
            var stmt = conn.prepareStatement(sql);
            var result = stmt.executeQuery()
        ) {
            if (!result.next()) {
                fail("No results found in the property table.");
            }

            assertEquals(1, result.getInt(1));
        }
    }
}
