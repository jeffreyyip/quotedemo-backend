package com.jy.quotedemo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface QuoteRepository {

    Flux<Quote> allQuote();

    Mono<Void> saveQuote(Mono<Quote> quote);

    List<Quote> lastQuote(int cnt);
}
