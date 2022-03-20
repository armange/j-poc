package br.com.armange.jpoc.spring.batch.processing;

import br.com.armange.jpoc.spring.batch.domain.dto.TransactionDto;
import org.springframework.batch.item.ItemProcessor;

public class Csv2XmlItemProcessor implements ItemProcessor<TransactionDto, TransactionDto> {

    public TransactionDto process(TransactionDto item) {
        return item;
    }
}
