package com.jy.quotedemo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.ipc.netty.http.server.HttpServer;

import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.method;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;

@SpringBootApplication
@EnableAutoConfiguration
public class ServerApp {

    public static final String HOST = "localhost";
    public static final int PORT = 8080;
    StringRedisTemplate template;
    boolean isProd = false;

    public ServerApp(){
        this.isProd = true;
    }
    public ServerApp(boolean isProd){
            this.isProd = isProd;
    }

    @Bean
    StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    public static void main(String[] args) throws Exception{
        ServerApp server = new ServerApp(true);
        server.initializeRedis();
        server.startReactorServer();

        System.out.println("Press ENTER to exit.");
        System.in.read();
    }

    public void initializeRedis(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(ServerApp.class);
        template = ctx.getBean(StringRedisTemplate.class);
    }

    public RouterFunction<ServerResponse> routingFunction() {
        QuoteRepository repository;
                if (isProd) {
                    repository = new RedisQuoteRepository(template);
                }else{
                    repository = new MockQuoteRepository();
                }
        QuoteHandler handler = new QuoteHandler(repository);

        return nest(path("/quote"),
                nest(accept(APPLICATION_JSON),
                        route(GET("/average/{cnt}"), handler::averageQuote)
                                .andRoute(method(HttpMethod.GET), handler::listQuote)
                ).andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::createQuote)
        );


    }

    public void startReactorServer() throws InterruptedException {
        RouterFunction<ServerResponse> route = routingFunction();
        HttpHandler httpHandler = toHttpHandler(route);

        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        HttpServer server = HttpServer.create(HOST, PORT);
        server.newHandler(adapter).block();
    }

}
