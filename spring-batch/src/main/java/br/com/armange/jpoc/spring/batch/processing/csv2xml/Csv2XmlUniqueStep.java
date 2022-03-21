package br.com.armange.jpoc.spring.batch.processing.csv2xml;

import br.com.armange.jpoc.spring.batch.domain.dto.TransactionDto;
import org.springframework.batch.item.ItemProcessor;

public class Csv2XmlUniqueStep implements ItemProcessor<TransactionDto, TransactionDto> {

    public TransactionDto process(TransactionDto item) {
        return item;
    }
}
