package br.com.armange.jpoc.spring.batch.reading.stateful;

import br.com.armange.jpoc.spring.batch.domain.dto.IdentificationDto;
import br.com.armange.jpoc.spring.batch.domain.dto.TransactionDto;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;
import java.util.List;

public class Dto2DtoItemReader implements ItemReader<IdentificationDto> {

    private final Iterator<TransactionDto> transactions;

    public Dto2DtoItemReader(final List<TransactionDto> transactions) {
        this.transactions = transactions.iterator();
    }
    @Override
    public IdentificationDto read() throws UnexpectedInputException,
            ParseException, NonTransientResourceException {
        System.out.println("Thread: " + Thread.currentThread().getName());
        return transactions.hasNext() ? new IdentificationDto(transactions.next()) : null;
    }
}
