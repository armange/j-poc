package br.com.armange.jpoc.spring.jdbc.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Data
@Builder
public class Book {

    @Id
    private Long id;
    private String name;

    public static Book fromRows(final Map<String, Object> rows) {
        return Book
                .builder()
                .id((Long.parseLong(rows.get("b_id").toString())))
                .name(rows.get("b_name").toString())
                .build();
    }
}
