package br.com.armange.jpoc.spring.batch.cache.bd.mapper;

import br.com.armange.jpoc.spring.batch.cache.bd.domain.dto.TransactionDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RecordFieldSetMapper implements FieldSetMapper<TransactionDto> {

    public TransactionDto mapFieldSet(FieldSet fieldSet) throws BindException {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyy");
        final String dateString = fieldSet.readString(3);

        return new TransactionDto(
                fieldSet.readInt(0),
                fieldSet.readString(1),
                fieldSet.readInt(2),
                LocalDate.parse(dateString, formatter).atStartOfDay(),
                fieldSet.readDouble(4)
        );
    }
}
