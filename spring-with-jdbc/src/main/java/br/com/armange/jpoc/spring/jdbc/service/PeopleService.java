package br.com.armange.jpoc.spring.jdbc.service;

import br.com.armange.jpoc.spring.jdbc.entity.People;
import br.com.armange.jpoc.spring.jdbc.mapper.PeopleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final JdbcTemplate jdbcTemplate;

    public Mono<People> findFetchDocumentsById(final Long peopleId) {
        return Mono.defer(() -> Mono.just(
                        Objects.requireNonNull(jdbcTemplate.query(
                                PeopleMapper.QUERY_PEOPLE_WITH_LOAN_AND_DOCUMENTS,
                                PeopleMapper.PEOPLE_WITH_DOCUMENTS_RS_EXTRACTOR, peopleId))))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
