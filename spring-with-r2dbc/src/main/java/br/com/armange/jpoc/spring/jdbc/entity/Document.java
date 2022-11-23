package br.com.armange.jpoc.spring.jdbc.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import reactor.core.publisher.Mono;

import java.util.Map;

@Data
public class Document implements Persistable<Long> {

    //TODO: A anotação ID sem a interface PERSISTABLE, faz com que o fluxo de persistência considere uma entidade com o atributo id preenchido, elegível apenas para UPDATE.
    @Id
    private Long id;
    private String value;

    @Column("document_type_id")
    private Long documentTypeId;

    @Column("people_id")
    private Long peopleId;

    //TODO: Não há opções de mapeamento dos relacionamentos.
    private DocumentType documentType;

    private People people;

    @Transient
    private boolean newEntity = true;

    //TODO: Se houverem atributos não presentes na tabela, será necessário indicar qual construtor vai gerar uma entidade, para que este contenha somente os atributos existentes na tabela.
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

    //TODO: Todos so mapeamentos entre entidades devem ser construídos manualmente para que as consultas possam retornar um objeto corretamente relacionado.
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
