package br.com.armange.jpoc.spring.jpa.service;

import br.com.armange.jpoc.spring.jpa.entity.People;
import br.com.armange.jpoc.spring.jpa.repository.PeopleRepository;
import br.com.armange.jpoc.spring.validation.PeopleValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;

@Service
@Validated
@RequiredArgsConstructor
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @InitBinder
    protected void initBinder(DataBinder binder) {
        binder.setValidator(new PeopleValidation());
    }

    public People persist(@Valid final People people) {
        return peopleRepository.save(people);
    }

    public Mono<People> save(@Valid final People people) {
        return Mono.defer(() -> Mono.just(peopleRepository.save(people)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<People> findWithDocumentsById(final Long peopleId) {
        return Mono.defer(() -> Mono.just(peopleRepository.findWithDocumentsById(peopleId)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<People> findFetchDocumentsById(final Long peopleId) {
        return Mono.defer(() -> Mono.just(peopleRepository.findFetchDocumentsById(peopleId)))
                .subscribeOn(Schedulers.boundedElastic());
    }
}
