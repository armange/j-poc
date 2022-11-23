package br.com.armange.jpoc.spring.r2dbc.repository;

import br.com.armange.jpoc.spring.r2dbc.entity.DocumentType;
import br.com.armange.jpoc.spring.r2dbc.tc.postgres.PostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DocumentTypeRepositoryIntegrationTest extends PostgresIntegrationTest {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Test
    void shouldSave() {
        final long id = 3L;
        final DocumentType documentType = documentTypeRepository
                .save(DocumentType
                        .builder()
                        .id(id)
                        .name("TE")
                        .build())
                .block();

        assertNotNull(documentTypeRepository.findById(id).block());
        assertDoesNotThrow(() -> documentTypeRepository.deleteById(id).block());
    }
}
