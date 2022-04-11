package br.com.armange.jpoc.spring.batch.cache.bd.configuration;

import br.com.armange.jpoc.spring.batch.cache.bd.domain.dto.TransactionDto;
import br.com.armange.jpoc.spring.batch.cache.bd.domain.repository.TransactionCacheRepository;
import br.com.armange.jpoc.spring.batch.cache.bd.mapper.RecordFieldSetMapper;
import br.com.armange.jpoc.spring.batch.cache.bd.processing.csv2xml.Csv2XmlUniqueStep;
import br.com.armange.jpoc.spring.batch.cache.bd.writing.csv2xml.TransactionXmlWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
@PropertySource("classpath:cache-db.properties")
public class Csv2XmlBatchConfig {

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Value("input/record.csv")
    private Resource inputCsv;

    @Value("file:build/xml/output.xml")
    private Resource outputXml;

    @Bean
    public ItemReader<TransactionDto> itemReader() throws UnexpectedInputException, ParseException {
        final FlatFileItemReader<TransactionDto> reader = new FlatFileItemReader<>();
        final DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        String[] tokens = { "id", "username", "userid", "transactiondate", "amount" };
        tokenizer.setNames(tokens);
        reader.setLinesToSkip(1);
        reader.setResource(inputCsv);

        final DefaultLineMapper<TransactionDto> lineMapper = new DefaultLineMapper<>();

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new RecordFieldSetMapper());
        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean
    public ItemProcessor<TransactionDto, TransactionDto> itemProcessor(
            final TransactionCacheRepository repository) {
        return new Csv2XmlUniqueStep(repository);
    }

    @Bean
    @Primary
    public Marshaller marshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setClassesToBeBound(TransactionDto.class);

        return marshaller;
    }

    @Bean
    public ItemWriter<TransactionDto> itemWriter(
            final TransactionCacheRepository repository,
            final Marshaller marshaller) {

        return new TransactionXmlWriter(
                repository,
                marshaller,
                "transactionRecords",
                outputXml
        );
    }

    @Bean("csv2Xml-unique-step")
    protected Step uniqueStep(final ItemReader<TransactionDto> reader,
                         final ItemProcessor<TransactionDto, TransactionDto> processor,
                         final ItemWriter<TransactionDto> writer) {
        return steps.get("csv2Xml-unique-step")
                .<TransactionDto, TransactionDto> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = "csv2Xml")
    public Job job(@Qualifier("csv2Xml-unique-step") final Step step1) {
        return jobs.get("csv2Xml").start(step1).build();
    }
}
