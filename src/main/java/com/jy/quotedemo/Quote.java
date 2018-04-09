package com.jy.quotedemo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Quote {
    private final String symbol;
    private final long timestamp;
    private final double price;

    public Quote(@JsonProperty("symbol") String symbol, @JsonProperty("price") double price, @JsonProperty("timestamp") long timestamp){
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }


    public double getPrice() {
        return price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString(){
        return "{" +
                "\"symbol\":\"" + symbol + "\"" +
                ",\"price\":" + price +
                ",\"timestamp\":" + timestamp +
                "}";
    }
}
