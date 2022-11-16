package br.com.armange.jpoc.spring.r2dbc.service;

import br.com.armange.jpoc.spring.r2dbc.entity.Document;
import br.com.armange.jpoc.spring.r2dbc.entity.DocumentType;
import br.com.armange.jpoc.spring.r2dbc.entity.People;
import br.com.armange.jpoc.spring.r2dbc.repository.DocumentRepository;
import br.com.armange.jpoc.spring.r2dbc.repository.DocumentTypeRepository;
import br.com.armange.jpoc.spring.r2dbc.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class DocumentService {

    public static final String QUERY = "select " +
            "    d.id d_id, " +
            "    d.value d_value, " +
            "    dt.id dt_id, " +
            "    dt.name dt_name, " +
            "    p.id p_id, " +
            "    p.name p_name " +
            " from document d " +
            "    inner join people p on p.id = d.people_id " +
            "    inner join document_type dt on dt.id = d.document_type_id " +
            " where d.id = $1";

    private final DatabaseClient client;
    private final PeopleRepository peopleRepository;
    private final DocumentRepository documentRepository;
    private final DocumentTypeRepository documentTypeRepository;

    @Transactional
    public Mono<Document> save(final Document newDocument) {

        return documentTypeRepository
                .save(newDocument.getDocumentType())
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(document -> Mono
                        .just(document)
                        .zipWith(peopleRepository
                                .save(newDocument.getPeople())
                                .subscribeOn(Schedulers.boundedElastic())))
                .flatMap(zip -> {
                    final DocumentType documentType = zip.getT1();
                    final People people = zip.getT2();
                    return documentRepository.save(Document.builderWithColumnsOnly()
                                    .id(newDocument.getId())
                                    .value(newDocument.getValue())
                                    .documentTypeId(documentType.getId())
                                    .peopleId(people.getId())
                                    .buildWithColumnsOnly())
                            .subscribeOn(Schedulers.boundedElastic());
                });
    }

    public Mono<Document> findFetchAllById(final Long documentId) {
        return client
                .sql(QUERY)
                .bind(0, documentId)
                .fetch()
                .all()
                .flatMap(Document::fromRowWithAllRelationsAsMono)
                .singleOrEmpty();
    }
}
