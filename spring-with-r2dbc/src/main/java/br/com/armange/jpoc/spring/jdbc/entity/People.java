package br.com.armange.jpoc.spring.jdbc.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class People implements Persistable<Long> {

    @Id
    private Long id;
    private String name;

    @Transient
    private List<Loan> loans = new ArrayList<>();

    @Transient
    private List<Document> documents = new ArrayList<>();

    @Transient
    private boolean newEntity = true;

    @Builder(builderMethodName = "builderWithColumnsOnly", buildMethodName = "buildWithColumnsOnly")
    @PersistenceCreator
    public People(final Long id,
                  final String name) {
        this.id = id;
        this.name = name;
    }

    @Builder(builderMethodName = "builderWithRelations", buildMethodName = "buildWithRelations")
    public People(final Long id,
                  final String name,
                  final List<Loan> loans,
                  final List<Document> documents) {
        this.id = id;
        this.name = name;
        this.loans = loans;
        this.documents = documents;
    }

    public static People fromRow(final Map<String, Object> row) {
        return People
                .builderWithColumnsOnly()
                .id((Long.parseLong(row.get("p_id").toString())))
                .name(row.get("p_name").toString())
                .buildWithColumnsOnly();
    }

    public static Mono<People> fromRowWithLoansAsMono(final List<Map<String, Object>> rows) {
        return Mono.just(People
                .builderWithRelations()
                .id((Long.parseLong(rows.get(0).get("p_id").toString())))
                .name(rows.get(0).get("p_name").toString())
                .loans(rows.stream().map(Loan::fromRow).collect(Collectors.toList()))
                .buildWithRelations());
    }

    public static Mono<People> fromRowWithDocumentsAsMono(final List<Map<String, Object>> rows) {
        return Mono.just(People
                .builderWithRelations()
                .id((Long.parseLong(rows.get(0).get("p_id").toString())))
                .name(rows.get(0).get("p_name").toString())
                .documents(rows.stream().map(Document::fromRowWithDocumentType).collect(Collectors.toList()))
                .buildWithRelations());
    }

    @Override
    public boolean isNew() {
        return newEntity;
    }
}
