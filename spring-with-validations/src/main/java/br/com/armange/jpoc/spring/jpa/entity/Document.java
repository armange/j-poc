package br.com.armange.jpoc.spring.jpa.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    private Long id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "documentTypeId", referencedColumnName = "id")
    private DocumentType documentType;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "peopleId", referencedColumnName = "id")
    private People people;
}
