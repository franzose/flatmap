package com.janiwanow.flatmap.realty.database;

import com.janiwanow.flatmap.console.event.PropertyDetailsParsed;
import com.janiwanow.flatmap.internal.sql.TestDbConnectionFactory;
import com.janiwanow.flatmap.realty.property.Area;
import com.janiwanow.flatmap.realty.property.Price;
import com.janiwanow.flatmap.realty.property.PropertyDetails;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public final class PropertyDetailsListenerSteps {
    private static final TestDbConnectionFactory DATABASE = TestDbConnectionFactory.INSTANCE;
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

    @Given("database already contains property details from {string}")
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

    @Then("^the database must still contain (\\d+) property details record(?:s)?$")
    public void ensurePropertyDetailsAreIgnored(int records) throws SQLException {
        try (
            var conn = DATABASE.getConnection();
            var stmt = conn.prepareStatement("SELECT COUNT(*) FROM property");
            var result = stmt.executeQuery()
        ) {
            result.next();

            assertEquals(records, result.getInt(1));
        }
    }
}
