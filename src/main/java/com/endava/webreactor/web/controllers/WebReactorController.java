package com.endava.webreactor.web.controllers;

import com.endava.webreactor.models.Person;
import com.endava.webreactor.services.PersonService;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@AllArgsConstructor
public class WebReactorController {

    private final PersonService personService;


    @GetMapping(value = "/finite", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Person> getFiniteNames() {
        return Flux.range(0, 10)
            .delayElements(Duration.ofMillis(500L + Faker.instance().random().nextInt(2000)))
            .map(integer -> personService.createPerson());
    }


    @GetMapping(value = "/infinite", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Person> getInfiniteNames() {
        return Flux.interval(Duration.ofMillis(300))
            .map(i -> personService.createPerson());
    }

}

