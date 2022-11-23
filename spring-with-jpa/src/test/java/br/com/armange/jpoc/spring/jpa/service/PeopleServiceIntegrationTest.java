package br.com.armange.jpoc.spring.jpa.service;

import br.com.armange.jpoc.spring.jpa.entity.People;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import spring.jpa.tc.postgres.PostgresIntegrationTest;

import java.util.function.Predicate;

@SpringBootTest
class PeopleServiceIntegrationTest extends PostgresIntegrationTest {

    @Autowired
    private PeopleService peopleService;

    @Test
    void shouldFetchDocumentsWithGraph() {
        final Mono<People> peopleMono = peopleService.findWithDocumentsById(1L);

        StepVerifier
                .create(peopleMono.log())
                .expectNextMatches(assertPeopleWithDocuments())
                .verifyComplete();
    }

    @Test
    void shouldFetchDocumentsWithQuery() {
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
