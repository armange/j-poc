package br.com.armange.jpoc.spring.batch.mapper;

import br.com.armange.jpoc.spring.batch.domain.dto.TransactionDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RecordFieldSetMapper implements FieldSetMapper<TransactionDto> {

    public TransactionDto mapFieldSet(FieldSet fieldSet) throws BindException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyy");
        TransactionDto transaction = new TransactionDto();

        transaction.setUsername(fieldSet.readString("username"));
        transaction.setUserId(fieldSet.readInt(1));
        transaction.setAmount(fieldSet.readDouble(3));
        String dateString = fieldSet.readString(2);
        transaction.setTransactionDate(LocalDate.parse(dateString, formatter).atStartOfDay());
        return transaction;
    }
}
