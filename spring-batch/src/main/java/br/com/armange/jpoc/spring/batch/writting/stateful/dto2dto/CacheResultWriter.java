package br.com.armange.jpoc.spring.batch.writting.stateful.dto2dto;

import br.com.armange.jpoc.spring.batch.domain.dto.IdWrapperDto;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;

import java.util.ArrayList;
import java.util.List;

public class CacheResultWriter implements ItemWriter<IdWrapperDto> {
    private StepExecution stepExecution;

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
        this.stepExecution.getExecutionContext().put("items", new ArrayList<IdWrapperDto>());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(List<? extends IdWrapperDto> items) throws Exception {
        final List<IdWrapperDto> cache = (List<IdWrapperDto>) this.stepExecution
                .getExecutionContext().get("items");
        assert cache != null;

        items.parallelStream()
                .forEach(i -> {
                    System.out.println(i);
                    cache.add(i);
                });
    }
}
