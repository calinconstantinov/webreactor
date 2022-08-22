package com.endava.webreactor.services;

import com.endava.webreactor.models.Person;
import net.datafaker.Faker;
import net.datafaker.Name;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    public Person createPerson() {
        Name name = Faker.instance().name();
        return new Person(name.firstName(), name.lastName());
    }

}
