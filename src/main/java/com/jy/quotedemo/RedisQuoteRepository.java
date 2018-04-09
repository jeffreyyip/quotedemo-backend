package com.jy.quotedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RedisQuoteRepository implements QuoteRepository {
    private StringRedisTemplate template;
    private final String quoteListName = "quoteList";
    final ObjectMapper readMapper = new ObjectMapper();
    final ObjectMapper writeMapper = new ObjectMapper();

    @Autowired
    public RedisQuoteRepository(StringRedisTemplate template) {
        this.template = template;
        System.out.println("RedisQuoteRepository: initialized with: " + this.template);
    }


    @Override
    public Flux<Quote> allQuote(){
        System.out.println("allQuote-REDIS");
            ListOperations listOperations = template.opsForList();
            List<String> quoteStr = listOperations.range(quoteListName, 0, -1);
            List<Quote> quotes = quoteStr.stream().map(str -> this.toQuote(readMapper, str)).collect(Collectors.toList());
            return Flux.fromIterable(quotes);

    }

    @Override
    public List<Quote> lastQuote(int cnt){
        System.out.println("lastQuote-REDIS");
        ListOperations listOperations = template.opsForList();
        List<String> quoteStr = listOperations.range(quoteListName, cnt*-1, -1);
        List<Quote> quotes = quoteStr.stream().map(str -> this.toQuote(readMapper, str)).collect(Collectors.toList());
        return quotes;
    }

    @Override
    public Mono<Void> saveQuote(Mono<Quote> quoteMono) {
        System.out.println("saveQuote-REDIS");
        ListOperations listOperations = template.opsForList();
        return quoteMono.doOnNext(quote -> {
            listOperations.rightPush(quoteListName, this.fromQuote(writeMapper, quote));
            System.out.println("Saved Quote: " + quote);
        }).thenEmpty(Mono.empty());
    }

    private Quote toQuote(ObjectMapper mapper, String str){
        Quote quote=null;
        try {
            quote = mapper.readValue(str, Quote.class);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return quote;
    }

    private String fromQuote(ObjectMapper mapper, Quote quote){
        String str=null;
        try {
            str = mapper.writeValueAsString(quote);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return str;
    }


}
