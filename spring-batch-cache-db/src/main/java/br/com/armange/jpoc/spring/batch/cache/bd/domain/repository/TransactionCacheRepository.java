package br.com.armange.jpoc.spring.batch.cache.bd.domain.repository;

import br.com.armange.jpoc.spring.batch.cache.bd.domain.dto.TransactionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionCacheRepository extends CrudRepository<TransactionDto, String> {}
