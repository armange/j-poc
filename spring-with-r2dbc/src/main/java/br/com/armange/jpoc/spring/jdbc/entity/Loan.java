package br.com.armange.jpoc.spring.jdbc.entity;

import lombok.Builder;
import lombok.Data;
import reactor.core.publisher.Mono;

import java.util.Map;

@Data
@Builder
public class Loan {

    private People people;
    private Book book;

    public static Mono<Loan> fromRowAsMono(final Map<String, Object> row) {
        return Mono
                .just(Loan
                        .builder()
                        .people(People.fromRow(row))
                        .book(Book.fromRows(row))
                        .build());
    }

    public static Loan fromRow(final Map<String, Object> row) {
        return Loan
                .builder()
                .people(People.fromRow(row))
                .book(Book.fromRows(row))
                .build();
    }
}
