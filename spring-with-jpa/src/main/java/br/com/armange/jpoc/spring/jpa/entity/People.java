package br.com.armange.jpoc.spring.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class People {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "people")
    private List<Document> documents = new ArrayList<>();
}