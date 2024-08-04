package br.com.armange.jpoc.spring.jpa.service;

import br.com.armange.jpoc.spring.jpa.entity.People;
import br.com.armange.jpoc.spring.jpa.repository.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import spring.jpa.tc.postgres.PostgresIntegrationTest;

import java.util.function.Predicate;

@SpringBootTest
@ActiveProfiles("postgres")
class PeopleServiceIntegrationTest extends PostgresIntegrationTest {

    @Autowired
    private PeopleService peopleService;

    @Test
    void shouldValidateOnSave() {
        final People person = new People();

        person.setName("reject");
        person.setId(20L);

        peopleService.persist(person);
//        final Mono<People> peopleMono = peopleService.save(person).doOnError(Throwable::printStackTrace);
//
//        StepVerifier
//                .create(peopleMono.log())
//                .expectNextMatches(assertPeopleWithDocuments())
//                .verifyComplete();
    }

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
