package br.com.armange.jpoc.spring.jdbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class People {

    private Long id;

    private String name;

    private List<Loan> loans = new ArrayList<>();

    private List<Document> documents = new ArrayList<>();
}