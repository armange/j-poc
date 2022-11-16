package br.com.armange.jpoc.spring.r2dbc.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import reactor.core.publisher.Mono;

import java.util.Map;

@Data
public class Document implements Persistable<Long> {

    @Id
    private Long id;
    private String value;

    @Column("document_type_id")
    private Long documentTypeId;

    @Column("people_id")
    private Long peopleId;

    @Transient
    @MappedCollection(idColumn = "document_type_id", keyColumn = "id")
    private DocumentType documentType;

    @Transient
    @MappedCollection(idColumn = "people_id", keyColumn = "id")
    private People people;

    @Transient
    private boolean newEntity = true;

    @Builder(builderMethodName = "builderWithColumnsOnly", buildMethodName = "buildWithColumnsOnly")
    @PersistenceCreator
    public Document(final Long id,
                    final String value,
                    final Long peopleId,
                    final Long documentTypeId) {
        this.id = id;
        this.value = value;
        this.peopleId = peopleId;
        this.documentTypeId = documentTypeId;
    }

    @Builder(builderMethodName = "builderWithRelations", buildMethodName = "buildWithRelations")
    public Document(final Long id,
                    final String value,
                    final DocumentType documentType,
                    final People people) {
        this.id = id;
        this.value = value;
        this.documentType = documentType;
        this.people = people;
    }

    public static Document fromRowWithDocumentType(final Map<String, Object> row) {
        return Document
                .builderWithRelations()
                .id(Long.parseLong(row.get("d_id").toString()))
                .value(row.get("d_value").toString())
                .documentType(DocumentType.builder()
                        .id(Long.parseLong(row.get("dt_id").toString()))
                        .name(row.get("dt_name").toString())
                        .build())
                .buildWithRelations();
    }

    public static Mono<Document> fromRowWithAllRelationsAsMono(final Map<String, Object> row) {
        return Mono.just(Document
                .builderWithRelations()
                .id(Long.parseLong(row.get("d_id").toString()))
                .value(row.get("d_value").toString())
                .documentType(DocumentType.builder()
                        .id(Long.parseLong(row.get("dt_id").toString()))
                        .name(row.get("dt_name").toString())
                        .build())
                .people(People.builderWithColumnsOnly()
                        .id(Long.parseLong(row.get("p_id").toString()))
                        .name(row.get("p_name").toString())
                        .buildWithColumnsOnly())
                .buildWithRelations());
    }

    @Override
    public boolean isNew() {
        return newEntity;
    }
}
