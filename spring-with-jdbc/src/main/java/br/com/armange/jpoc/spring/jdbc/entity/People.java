package br.com.armange.jpoc.spring.jdbc.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class People {

    private Long id;

    private String name;

    @Builder.Default
    @ToString.Exclude
    private List<Loan> loans = new ArrayList<>();

    @Builder.Default
    @ToString.Exclude
    private List<Document> documents = new ArrayList<>();
}