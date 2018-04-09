package com.jy.quotedemo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class MockQuoteRepository implements QuoteRepository{

    private final List<Quote> quotes = new ArrayList<>();


    public MockQuoteRepository(){
        quotes.add(new Quote("0001.HK", 90.50, 80905000000001L));
        quotes.add(new Quote("0001.HK", 91.25, 80905000000002L));

    }


    @Override
    public Flux<Quote> allQuote() {

        return Flux.fromIterable(this.quotes);
    }

    @Override
    public Mono<Void> saveQuote(Mono<Quote> quoteMono) {
        return quoteMono.doOnNext(quote -> {
            quotes.add(quote);
            System.out.println("Saved Quote: " + quote);
        }).thenEmpty(Mono.empty());
    }

    @Override
    public List<Quote> lastQuote(int cnt) {
        return quotes.subList(quotes.size() - cnt, quotes.size());
    }
}
