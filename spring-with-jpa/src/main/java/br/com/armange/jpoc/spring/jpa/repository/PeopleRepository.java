package br.com.armange.jpoc.spring.jpa.repository;

import br.com.armange.jpoc.spring.jpa.entity.People;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PeopleRepository extends CrudRepository<People, Long> {

    @EntityGraph(attributePaths = { "documents" })
    People findWithDocumentsById(Long id);

    @Query("select p from People p join fetch p.documents where p.id = ?1")
    People findFetchDocumentsById(Long id);
}
