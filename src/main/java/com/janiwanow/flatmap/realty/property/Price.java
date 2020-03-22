package com.janiwanow.flatmap.realty.property;

import java.util.Currency;
import java.util.Objects;

/**
 * Apartment price, either purchase or rental.
 */
public final class Price {
    public final double amount;
    public final Currency currency;

    /**
     * @param currency currency in which this apartment is offered
     * @param value price amount
     */
    public Price(Currency currency, double value) {
        Objects.requireNonNull(currency, "Currency must not be null.");
        this.currency = currency;
        this.amount = Math.max(0, value);
    }

    public static Price inRubles(double value) {
        return new Price(Currency.getInstance("RUB"), value);
    }

    public static Price inDollars(double value) {
        return new Price(Currency.getInstance("USD"), value);
    }

    public static Price inEuros(double value) {
        return new Price(Currency.getInstance("EUR"), value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return Double.compare(price.amount, amount) == 0 &&
            currency.equals(price.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
}
