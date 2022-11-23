package br.com.armange.jpoc.spring.jdbc.repository;

import br.com.armange.jpoc.spring.jdbc.entity.Loan;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends ReactiveCrudRepository<Loan, Long> {

}
