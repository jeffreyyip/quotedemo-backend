package com.jy.quotedemo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteAverage {
    private double average;

    public QuoteAverage(@JsonProperty("average") double average){
        this.average = average;
    }

    public double getAverage() {
        return average;
    }


    @Override
    public String toString(){
        return "QuoteAverage{" +
                "average=" + average +
                '}';
    }
}
