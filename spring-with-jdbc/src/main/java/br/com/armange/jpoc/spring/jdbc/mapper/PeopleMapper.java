package br.com.armange.jpoc.spring.jdbc.mapper;

import br.com.armange.jpoc.spring.jdbc.entity.Document;
import br.com.armange.jpoc.spring.jdbc.entity.DocumentType;
import br.com.armange.jpoc.spring.jdbc.entity.People;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PeopleMapper {
    public static final String QUERY_PEOPLE_WITH_LOAN_AND_DOCUMENTS = "select " +
            "p.id p_id, " +
            "p.name p_name," +
            "b.id b_id, " +
            "b.name b_name, " +
            "d.id d_id, " +
            "d.value d_value, " +
            "dt.id dt_id, " +
            "dt.name dt_name " +
            "from loan l " +
            "inner join people p on p.id = l.people_id " +
            "inner join book b on b.id = l.book_id " +
            "inner join document d on d.people_id = p.id " +
            "inner join document_type dt on dt.id = d.document_type_id " +
            "where p.id = %d";

    public static final ResultSetExtractor<People> PEOPLE_WITH_DOCUMENTS_RS_EXTRACTOR = rs -> {
        final Map<Long, People> peopleMap = new HashMap<>();
        final Map<Long, Document> documentMap = new HashMap<>();
        final Map<Long, DocumentType> documentTypeMap = new HashMap<>();
        final AtomicReference<People> peopleRef = new AtomicReference<>();

        while (rs.next()) {
            peopleRef.set(readPeople(rs, peopleMap));

            final People people = peopleRef.get();
            final Document document = readDocument(rs, documentMap, people);

            readDocumentType(rs, documentTypeMap, document);

            people.getDocuments().add(document);
        }

        return peopleRef.get();
    };

    private static People readPeople(final ResultSet rs,
                                     final Map<Long, People> peopleMap) throws SQLException {
        final Long peopleId = rs.getLong("p_id");
        final People people = peopleMap.getOrDefault(peopleId, People.builder().build());

        if (people.getId() == null) {
            people.setId(peopleId);
            people.setName(rs.getString("p_name"));
            peopleMap.put(peopleId, people);
        }
        return people;
    }

    private static Document readDocument(final ResultSet rs,
                                         final Map<Long, Document> documentMap,
                                         final People people) throws SQLException {
        final Long documentId = rs.getLong("d_id");
        final Document document = documentMap.getOrDefault(documentId, Document.builder().build());

        if (document.getId() == null) {
            document.setId(documentId);
            document.setPeople(people);
            document.setPeopleId(people.getId());
            document.setValue(rs.getString("d_value"));
            documentMap.put(documentId, document);
        }
        return document;
    }

    private static void readDocumentType(final ResultSet rs,
                                         final Map<Long, DocumentType> documentTypeMap,
                                         final Document document) throws SQLException {
        final Long documentTypeId = rs.getLong("dt_id");
        final DocumentType documentType = documentTypeMap.get(documentTypeId);

        if (documentType.getId() == null) {
            documentType.setId(documentTypeId);
            documentType.setName(rs.getString("dt_name"));
            document.setDocumentType(documentType);
            document.setDocumentTypeId(documentTypeId);
            documentTypeMap.put(documentTypeId, documentType);
        }
    }
}
