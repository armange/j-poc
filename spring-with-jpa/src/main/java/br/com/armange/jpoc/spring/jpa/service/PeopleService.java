package br.com.armange.jpoc.spring.jpa.service;

import br.com.armange.jpoc.spring.jpa.entity.People;
import br.com.armange.jpoc.spring.jpa.repository.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PeopleService {

    private final PeopleRepository peopleRepository;

    public Mono<People> findFetchDocumentsById(final Long peopleId) {
        return Mono.defer(() -> Mono.just(peopleRepository.findFetchDocumentsById(peopleId)))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
