package br.com.armange.jpoc.spring.batch.configuration;

import br.com.armange.jpoc.spring.batch.domain.dto.IdWrapperDto;
import br.com.armange.jpoc.spring.batch.domain.dto.IdentificationDto;
import br.com.armange.jpoc.spring.batch.domain.dto.TransactionDto;
import br.com.armange.jpoc.spring.batch.processing.Dto2DtoItemProcessor;
import br.com.armange.jpoc.spring.batch.reading.stateful.Dto2DtoItemReader;
import br.com.armange.jpoc.spring.batch.writting.stateful.Dto2DtoItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.util.Arrays;
import java.util.List;

public class DtoMultiThread2DtoConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    private final List<TransactionDto> transactions = Arrays.asList(
            new TransactionDto(1),
            new TransactionDto(2),
            new TransactionDto(3),
            new TransactionDto(4),
            new TransactionDto(5),
            new TransactionDto(6),
            new TransactionDto(7),
            new TransactionDto(8),
            new TransactionDto(9),
            new TransactionDto(10),
            new TransactionDto(11),
            new TransactionDto(12),
            new TransactionDto(13));

    @Bean("dto2Dto-unique-reader")
    public ItemReader<IdentificationDto> itemReader()
            throws UnexpectedInputException, ParseException {

        return new Dto2DtoItemReader(transactions);
    }

    @Bean("dto2Dto-unique-processor")
    public ItemProcessor<IdentificationDto, IdWrapperDto> itemProcessor() {
        return new Dto2DtoItemProcessor();
    }

    @Bean("dto2Dto-unique-writer")
    public ItemWriter<IdWrapperDto> itemWriter() {
        return new Dto2DtoItemWriter();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }

    @Bean("dto2Dto-unique-step")
    protected Step uniqueStep(
            @Qualifier("dto2Dto-unique-reader")
            final ItemReader<IdentificationDto> reader,
            @Qualifier("dto2Dto-unique-processor")
            final ItemProcessor<IdentificationDto, IdWrapperDto> processor,
            @Qualifier("dto2Dto-unique-writer")
            final ItemWriter<IdWrapperDto> writer,
            final TaskExecutor taskExecutor) {
        return steps.get("dto2Dto-unique-step").
                        <IdentificationDto, IdWrapperDto> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean(name = "dto2Dto-job")
    public Job job(@Qualifier("dto2Dto-unique-step") Step step1) {
        return jobs.get("dto2Dto-job").start(step1).build();
    }
}
