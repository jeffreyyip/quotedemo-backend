package com.jy.quotedemo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

public class QuoteHandlerTests {

    WebTestClient testClient;

    @Before
    public void createTestClient() {
        ServerApp server = new ServerApp(false);
        this.testClient = WebTestClient.bindToRouterFunction(server.routingFunction())
                .configureClient()
                .baseUrl("http://localhost:8080/quote")
                .build();
    }

    @Test
    public void createQuote() throws Exception {

        List<Quote> quotes = mockQuotes();


        this.testClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(quotes.get(0))
                .exchange()
                .expectStatus().isOk();

    }

    @Test
    public void listQuote() throws Exception {
        this.testClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Quote.class).hasSize(2).returnResult();
    }

    @Test
    public void getAverage() throws Exception {

        List<Quote> quotes = mockQuotes();


        this.testClient.post()
                .uri("/")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(quotes.get(0))
                .exchange()
                .expectStatus().isOk();

        this.testClient.get()
                .uri("/average/2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(QuoteAverage.class).hasSize(1).returnResult();
    }

    @Test
    public void testCalAverage() throws Exception{
        QuoteHandler handler = new QuoteHandler(null);
        double average = handler.calAverage(mockQuotes());
        Assert.assertEquals(90.9, average, 0.0);

    }

    private List<Quote> mockQuotes(){
        List<Quote> quotes = new ArrayList<>();

        quotes.add(new Quote("0001.HK", 90.50, 80905000000003L));
        quotes.add(new Quote("0001.HK", 91.30, 80905000000004L));

        return quotes;
    }


}
