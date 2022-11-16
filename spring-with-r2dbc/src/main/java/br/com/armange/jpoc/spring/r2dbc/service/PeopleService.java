package br.com.armange.jpoc.spring.r2dbc.service;

import br.com.armange.jpoc.spring.r2dbc.entity.People;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PeopleService {
    public static final String QUERY = "select " +
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

    private final DatabaseClient client;

    public Mono<People> findFetchLoansById(final Long peopleId) {
        return client
                .sql(String.format(QUERY, peopleId))
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("p_id"))
                .flatMap(People::fromRowWithLoansAsMono)
                .singleOrEmpty();
    }

    public Mono<People> findFetchDocumentsById(final Long peopleId) {
        return client
                .sql(String.format(QUERY, peopleId))
                .fetch()
                .all()
                .bufferUntilChanged(result -> result.get("p_id"))
                .flatMap(People::fromRowWithDocumentsAsMono)
                .singleOrEmpty();
    }
}
