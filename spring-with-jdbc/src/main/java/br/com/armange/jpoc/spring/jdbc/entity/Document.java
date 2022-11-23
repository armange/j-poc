package br.com.armange.jpoc.spring.jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    private Long id;

    private String value;

    private Long documentTypeId;

    private Long peopleId;

    private DocumentType documentType;

    private People people;
}
