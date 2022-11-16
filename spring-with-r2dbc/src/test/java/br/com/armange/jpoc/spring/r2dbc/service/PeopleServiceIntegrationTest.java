package br.com.armange.jpoc.spring.r2dbc.service;

import br.com.armange.jpoc.spring.r2dbc.entity.People;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Predicate;

@SpringBootTest
class PeopleServiceIntegrationTest {

    @Autowired
    private PeopleService peopleService;

    @Test
    void shouldFetchLoans() {
        final Mono<People> peopleMono = peopleService.findFetchLoansById(1L);

        StepVerifier
                .create(peopleMono.log())
                .expectNextMatches(assertPeopleWithLoans())
                .verifyComplete();
    }

    private static Predicate<People> assertPeopleWithLoans() {
        return people -> people.getLoans() != null && !people.getLoans().isEmpty();
    }

    @Test
    void shouldFetchDocuments() {
        final Mono<People> peopleMono = peopleService.findFetchDocumentsById(1L);

        StepVerifier
                .create(peopleMono.log())
                .expectNextMatches(assertPeopleWithDocuments())
                .verifyComplete();
    }

    private static Predicate<People> assertPeopleWithDocuments() {
        return people -> people.getDocuments() != null && !people.getDocuments().isEmpty();
    }
}
