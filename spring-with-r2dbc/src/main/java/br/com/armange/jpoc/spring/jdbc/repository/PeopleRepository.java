package br.com.armange.jpoc.spring.jdbc.repository;

import br.com.armange.jpoc.spring.jdbc.entity.People;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

//TODO: A LIB do JPA causa conflitos/falhas no contexto do Spring.
@Repository
public interface PeopleRepository extends ReactiveCrudRepository<People, Long> {

}
