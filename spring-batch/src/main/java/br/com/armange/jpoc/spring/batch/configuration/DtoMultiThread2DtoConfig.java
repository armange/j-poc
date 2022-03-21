package br.com.armange.jpoc.spring.batch.configuration;

import br.com.armange.jpoc.spring.batch.domain.dto.IdWrapperDto;
import br.com.armange.jpoc.spring.batch.domain.dto.IdentificationDto;
import br.com.armange.jpoc.spring.batch.domain.dto.TransactionDto;
import br.com.armange.jpoc.spring.batch.processing.dto2dto.Dto2DtoStep1;
import br.com.armange.jpoc.spring.batch.processing.dto2dto.Dto2DtoStep2;
import br.com.armange.jpoc.spring.batch.processing.dto2dto.Dto2DtoStep3;
import br.com.armange.jpoc.spring.batch.processing.dto2dto.Dto2DtoStep4;
import br.com.armange.jpoc.spring.batch.reading.stateful.dto2dto.IdDto2IdDtoReader;
import br.com.armange.jpoc.spring.batch.reading.stateful.dto2dto.TransactionDto2IdDtoReader;
import br.com.armange.jpoc.spring.batch.writting.stateful.dto2dto.CacheResultWriter;
import br.com.armange.jpoc.spring.batch.writting.stateful.dto2dto.DoNothingWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
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

    @Bean("transactionDto2IdDtoReader")
    public ItemReader<IdentificationDto> transactionDto2IdDtoReader()
            throws UnexpectedInputException, ParseException {

        return new TransactionDto2IdDtoReader(transactions);
    }

    @Bean("idDto2IdDtoReader")
    public ItemReader<IdWrapperDto> idDto2IdDtoReader()
            throws UnexpectedInputException, ParseException {

        return new IdDto2IdDtoReader();
    }

    @Bean("dto2Dto-ip1")
    public ItemProcessor<IdentificationDto, IdWrapperDto> step1() {
        return new Dto2DtoStep1();
    }

    @Bean("dto2Dto-ip2")
    public ItemProcessor<IdWrapperDto, IdWrapperDto> step2() {
        return new Dto2DtoStep2();
    }

    @Bean("dto2Dto-ip3")
    public ItemProcessor<IdWrapperDto, IdWrapperDto> step3() {
        return new Dto2DtoStep3();
    }

    @Bean("dto2Dto-ip4")
    public ItemProcessor<IdWrapperDto, IdWrapperDto> step4() {
        return new Dto2DtoStep4();
    }

    @Bean("cacheResultWriter")
    public ItemWriter<IdWrapperDto> cacheResultWriter() {
        return new CacheResultWriter();
    }

    @Bean("doNothingWriter")
    public ItemWriter<IdWrapperDto> doNothingWriter() {
        return new DoNothingWriter();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }


    @Bean(name = "dto2Dto-job")
    public Job job(
            @Qualifier("dto2Dto-flow1") final Flow flow1,
            @Qualifier("dto2Dto-flow2and3") final Flow flow2and3,
            @Qualifier("dto2Dto-step4") final Step step4) {
        return jobs.get("dto2Dto-job")
                .start(flow1)
                .next(flow2and3)
                .next(step4)
                .build()
                .build();
    }

    @Bean("dto2Dto-flow1")
    public Flow flow1(@Qualifier("dto2Dto-step1") final Step step1) {
        return new FlowBuilder<SimpleFlow>("flow1")
                .start(step1)
                .build();
    }

    @Bean("dto2Dto-flow2and3")
    public Flow flow2and3(@Qualifier("dto2Dto-flow2") final Flow flow2,
                          @Qualifier("dto2Dto-flow3") final Flow flow3) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor())
                .add(flow2, flow3)
                .build();
    }

    @Bean("dto2Dto-flow2")
    public Flow flow2(@Qualifier("dto2Dto-step2") final Step step2) {
        return new FlowBuilder<SimpleFlow>("flow2")
                .start(step2)
                .build();
    }

    @Bean("dto2Dto-flow3")
    public Flow flow3(@Qualifier("dto2Dto-step3") final Step step3) {
        return new FlowBuilder<SimpleFlow>("flow3")
                .start(step3)
                .build();
    }

    @Bean("dto2Dto-step1")
    protected Step step1(
            @Qualifier("transactionDto2IdDtoReader")
            final ItemReader<IdentificationDto> reader,
            @Qualifier("dto2Dto-ip1")
            final ItemProcessor<IdentificationDto, IdWrapperDto> processor,
            @Qualifier("cacheResultWriter")
            final ItemWriter<IdWrapperDto> writer,
            final TaskExecutor taskExecutor) {
        return steps.get("dto2Dto-step1").
                        <IdentificationDto, IdWrapperDto> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor)
                .listener(promotionListener())
                .build();
    }

    @Bean("dto2Dto-step2")
    protected Step step2(
            @Qualifier("idDto2IdDtoReader")
            final ItemReader<IdWrapperDto> reader,
            @Qualifier("dto2Dto-ip2")
            final ItemProcessor<IdWrapperDto, IdWrapperDto> processor,
            @Qualifier("doNothingWriter")
            final ItemWriter<IdWrapperDto> writer) {
        return steps.get("dto2Dto-step2").
                        <IdWrapperDto, IdWrapperDto> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean("dto2Dto-step3")
    protected Step step3(
            @Qualifier("idDto2IdDtoReader")
            final ItemReader<IdWrapperDto> reader,
            @Qualifier("dto2Dto-ip3")
            final ItemProcessor<IdWrapperDto, IdWrapperDto> processor,
            @Qualifier("doNothingWriter")
            final ItemWriter<IdWrapperDto> writer) {
        return steps.get("dto2Dto-step3").
                        <IdWrapperDto, IdWrapperDto> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean("dto2Dto-step4")
    protected Step step4(
            @Qualifier("idDto2IdDtoReader")
            final ItemReader<IdWrapperDto> reader,
            @Qualifier("dto2Dto-ip4")
            final ItemProcessor<IdWrapperDto, IdWrapperDto> processor,
            @Qualifier("doNothingWriter")
            final ItemWriter<IdWrapperDto> writer) {
        return steps.get("dto2Dto-step4").
                        <IdWrapperDto, IdWrapperDto> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public ExecutionContextPromotionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

        listener.setKeys(new String[] {"items"});

        return listener;
    }
}
