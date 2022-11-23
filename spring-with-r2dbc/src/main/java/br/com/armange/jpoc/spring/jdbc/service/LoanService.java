package br.com.armange.jpoc.spring.jdbc.service;

import br.com.armange.jpoc.spring.jdbc.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final DatabaseClient client;

    public Flux<Loan> findFetchAllByPeople(final Long peopleId) {
        final String query = "select " +
                "p.id p_id, " +
                "p.name p_name," +
                "b.id b_id, " +
                "b.name b_name " +
                "from loan l " +
                "inner join people p on p.id = l.people_id " +
                "inner join book b on b.id = l.book_id " +
                "where p.id = %d";

        return client
                .sql(String.format(query, peopleId))
                .fetch()
                .all()
                .flatMap(Loan::fromRowAsMono);
    }
}
