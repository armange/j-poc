package br.com.armange.jpoc.spring.jdbc.service;

import br.com.armange.jpoc.spring.jdbc.entity.Loan;
import br.com.armange.jpoc.spring.jdbc.tc.postgres.PostgresIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoanServiceIntegrationTest extends PostgresIntegrationTest {

    @Autowired
    private LoanService loanService;

    @Test
    void shouldFetchAll() {
        final List<Loan> loans = loanService
                .findFetchAllByPeople(1L)
                .collectList()
                .block();

        assertThat(loans).isNotNull().hasSize(2);
    }
}
