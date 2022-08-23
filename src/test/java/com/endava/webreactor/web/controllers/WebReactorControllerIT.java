package com.endava.webreactor.web.controllers;

import com.endava.webreactor.models.Person;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

class WebReactorControllerIT {

    @Test
    void testFinite_One() {
        Flux<Person> personFlux = WebClient.builder()
            .build()
            .get()
            .uri("http://localhost:8080/finite")
            .retrieve()
            .bodyToFlux(Person.class);

        List<Person> persons = personFlux.collectList().block();

        assertNotNull(persons);
        assertEquals(10, persons.size());
    }


    @Test
    void testFinite_Two() {
        WebTestClient.bindToServer()
            .responseTimeout(Duration.ofSeconds(30))
            .build()
            .get()
            .uri("http://localhost:8080/finite")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Person.class).hasSize(10);
    }


    @Test
    void testInfinite_One() {
        Flux<Person> personFlux = WebClient.builder()
            .build()
            .get()
            .uri("http://localhost:8080/infinite")
            .retrieve()
            .bodyToFlux(Person.class)
            .take(5);

        List<Person> persons = personFlux.collectList().block();

        assertNotNull(persons);
        assertEquals(5, persons.size());
    }


    @Test
    void testInfinite_Two() {
        FluxExchangeResult<Person> personFluxExchangeResult = WebTestClient.bindToServer()
            .responseTimeout(Duration.ofSeconds(30))
            .build()
            .get()
            .uri("http://localhost:8080/infinite")
            .accept(TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .returnResult(Person.class);

        StepVerifier.create(personFluxExchangeResult.getResponseBody())
            .expectNextCount(25)
            .consumeNextWith(System.out::println)
            .expectNextCount(1)
            .thenCancel()
            .verify();
    }

}

