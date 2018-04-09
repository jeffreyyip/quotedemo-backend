package com.jy.quotedemo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;
import java.util.OptionalDouble;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class QuoteHandler {

    private final QuoteRepository repository;

    public QuoteHandler(QuoteRepository repository){
        this.repository = repository;
    }


    public Mono<ServerResponse> createQuote(ServerRequest request){
        Mono<Quote> quote = request.bodyToMono(Quote.class);
        return ServerResponse.ok().build(repository.saveQuote(quote));
    }

    public Mono<ServerResponse> listQuote(ServerRequest request){
        Flux<Quote> quote = this.repository.allQuote();
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(quote, Quote.class);
    }

    public Mono<ServerResponse> averageQuote(ServerRequest request){
        int cnt = Integer.valueOf(request.pathVariable("cnt"));

        List<Quote> quotes = this.repository.lastQuote(cnt);
        double average = calAverage(quotes);
        Mono<QuoteAverage> averageMono= Mono.justOrEmpty(new QuoteAverage(average));
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(averageMono, QuoteAverage.class);
    }

    public double calAverage(List<Quote> quotes){
        OptionalDouble optDouble = quotes.stream().mapToDouble(e -> e.getPrice()).average();
        return optDouble.isPresent()?optDouble.getAsDouble():0.0;
    }

}
