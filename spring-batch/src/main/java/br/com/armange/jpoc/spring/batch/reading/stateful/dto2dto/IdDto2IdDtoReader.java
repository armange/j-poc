package br.com.armange.jpoc.spring.batch.reading.stateful.dto2dto;

import br.com.armange.jpoc.spring.batch.domain.dto.IdWrapperDto;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.*;

import java.util.Iterator;
import java.util.List;

public class IdDto2IdDtoReader implements ItemReader<IdWrapperDto> {
    private Iterator<IdWrapperDto> transactions;

    @SuppressWarnings("unchecked")
    @BeforeStep
    public void retrieveSharedData(StepExecution stepExecution) {
        final JobExecution jobExecution = stepExecution.getJobExecution();
        final ExecutionContext jobContext = jobExecution.getExecutionContext();
        final List<IdWrapperDto> items = (List<IdWrapperDto>) jobContext.get("items");

        assert items != null;
        transactions = items.iterator();
    }

    @Override
    public IdWrapperDto read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
        System.out.println("Thread: " + Thread.currentThread().getName());

        return transactions.hasNext() ? transactions.next() : null;
    }
}
