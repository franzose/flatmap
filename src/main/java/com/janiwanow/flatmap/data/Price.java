package com.janiwanow.flatmap.data;

import java.util.Currency;

public final class Price {
    public final double amount;
    public final Currency currency;

    public Price(Currency currency, double value) {
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
}
