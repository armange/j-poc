package br.com.armange.jpoc.spring.batch.cache.bd.processing.csv2xml;

import br.com.armange.jpoc.spring.batch.cache.bd.domain.dto.TransactionDto;
import br.com.armange.jpoc.spring.batch.cache.bd.domain.repository.TransactionCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class Csv2XmlUniqueStep implements ItemProcessor<TransactionDto, TransactionDto> {

    private final TransactionCacheRepository transactionCacheRepository;

    public TransactionDto process(final TransactionDto item) {
        transactionCacheRepository.save(item);

        return item;
    }
}
