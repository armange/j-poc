package br.com.armange.jpoc.spring.r2dbc.repository;

import br.com.armange.jpoc.spring.r2dbc.entity.Document;
import br.com.armange.jpoc.spring.r2dbc.entity.DocumentType;
import br.com.armange.jpoc.spring.r2dbc.entity.People;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DocumentRepositoryIntegrationTest {

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Test
    void shouldSaveDocument() {
        final Optional<Mono<Document>> documentMono = documentTypeRepository
                .findById(1L)
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(document -> Mono
                        .just(document)
                        .zipWith(peopleRepository
                                .findById(1L)
                                .subscribeOn(Schedulers.boundedElastic())))
                .map(zip -> {
                    final DocumentType documentType = zip.getT1();
                    final People people = zip.getT2();
                    return documentRepository.save(Document.builderWithColumnsOnly()
                            .id(4L)
                            .value("123111")
                            .documentTypeId(documentType.getId())
                            .peopleId(people.getId())
//                            .documentType(documentType)
//                            .people(people)
                            .buildWithColumnsOnly())
                            .subscribeOn(Schedulers.boundedElastic());
                }).blockOptional();

        assertTrue(documentMono.isPresent());
        assertNotNull(documentMono.get().block());
        assertDoesNotThrow(() -> documentRepository.deleteById(4L).block());
    }
}
