package com.endava.webreactor.web.controllers;

import com.endava.webreactor.models.Person;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
            .responseTimeout(Duration.ofSeconds(10))
            .build()
            .get()
            .uri("http://localhost:8080/finite")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBodyList(Person.class)
            .hasSize(10);
    }

}

