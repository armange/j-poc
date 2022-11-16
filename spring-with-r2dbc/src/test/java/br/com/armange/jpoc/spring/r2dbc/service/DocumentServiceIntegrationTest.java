package br.com.armange.jpoc.spring.r2dbc.service;

import br.com.armange.jpoc.spring.r2dbc.entity.Document;
import br.com.armange.jpoc.spring.r2dbc.entity.DocumentType;
import br.com.armange.jpoc.spring.r2dbc.entity.People;
import br.com.armange.jpoc.spring.r2dbc.repository.DocumentRepository;
import br.com.armange.jpoc.spring.r2dbc.repository.DocumentTypeRepository;
import br.com.armange.jpoc.spring.r2dbc.repository.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DocumentServiceIntegrationTest {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void shouldDoCommitSavingRelations() {
        final Long documentId = 3L;
        final String documentValue = "789987";
        final Long peopleId = 4L;
        final String peopleName = "People 4";
        final Long documentTypeId = 4L;
        final String documentTypeName = "CTPS";
        final Document newDocument = Document.builderWithRelations()
                .id(documentId)
                .value(documentValue)
                .people(People.builderWithColumnsOnly()
                        .id(peopleId)
                        .name(peopleName)
                        .buildWithColumnsOnly())
                .documentType(DocumentType.builder()
                        .id(documentTypeId)
                        .name(documentTypeName)
                        .build())
                .buildWithRelations();

        final Document ctps = documentService.save(newDocument).block();

        assertNotNull(ctps);
        StepVerifier
                .create(documentService.findFetchAllById(documentId).log())
                .expectNextMatches(document ->
                    documentValue.equals(document.getValue())
                            && document.getDocumentType() != null
                            && documentTypeId.equals(document.getDocumentType().getId())
                            && documentTypeName.equals(document.getDocumentType().getName())
                            && document.getPeople() != null
                            && peopleId.equals(document.getPeople().getId())
                            && peopleName.equals(document.getPeople().getName())
                )
                .verifyComplete();
        assertDoesNotThrow(() -> documentRepository.deleteById(3L).block());
        assertDoesNotThrow(() -> peopleRepository.deleteById(4L).block());
        assertDoesNotThrow(() -> documentTypeRepository.deleteById(4L).block());
    }

    @Test
    void shouldDoRollbackTryingSaveRelations() {
        final long peopleId = 4L;
        final long documentTypeId = 4L;
        final long documentId = 2L;
        final Document newDocument = Document.builderWithRelations()
                .id(documentId)
                .value("789987")
                .people(People.builderWithColumnsOnly()
                        .id(peopleId)
                        .name("People 4")
                        .buildWithColumnsOnly())
                .documentType(DocumentType.builder()
                        .id(documentTypeId)
                        .name("CTPS")
                        .build())
                .buildWithRelations();

        StepVerifier
                .create(documentService.save(newDocument).log())
                .expectErrorMatches(err -> err instanceof org.springframework.dao.DataIntegrityViolationException)
                .verify();

        StepVerifier
                .create(peopleRepository.findById(peopleId).log())
                .verifyComplete();

        StepVerifier
                .create(documentTypeRepository.findById(documentTypeId).log())
                .verifyComplete();
    }

    @Test
    void shouldFindFetchAllById() {
        final Long documentId = 1L;
        final String documentValue = "123456789";
        final Long peopleId = 1L;
        final String peopleName = "People 1";
        final Long documentTypeId = 2L;
        final String documentTypeName = "RG";

        StepVerifier
                .create(documentService.findFetchAllById(documentId).log())
                .expectNextMatches(document ->
                        documentValue.equals(document.getValue())
                                && document.getDocumentType() != null
                                && documentTypeId.equals(document.getDocumentType().getId())
                                && documentTypeName.equals(document.getDocumentType().getName())
                                && document.getPeople() != null
                                && peopleId.equals(document.getPeople().getId())
                                && peopleName.equals(document.getPeople().getName())
                )
                .verifyComplete();
    }
}
