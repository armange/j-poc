package br.com.armange.jpoc.spring.jdbc.repository;

import br.com.armange.jpoc.spring.jdbc.entity.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends ReactiveCrudRepository<Document, Long> {

}
