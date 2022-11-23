package br.com.armange.jpoc.spring.r2dbc.repository;

import br.com.armange.jpoc.spring.r2dbc.entity.People;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

//TODO: A LIB do JPA causa conflitos/falhas no contexto do Spring.
@Repository
public interface PeopleRepository extends ReactiveCrudRepository<People, Long> {

}
