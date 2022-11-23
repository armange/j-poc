package br.com.armange.jpoc.spring.jdbc.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("document_type")
public class DocumentType implements Persistable<Long> {

    @Id
    private Long id;
    private String name;

    @Transient
    private boolean newEntity = true;

    @Builder
    @PersistenceCreator
    public DocumentType(final Long id,
                        final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean isNew() {
        return newEntity;
    }
}


