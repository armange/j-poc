package br.com.armange.jpoc.spring.batch.cache.bd.writing.csv2xml;

import br.com.armange.jpoc.spring.batch.cache.bd.domain.dto.TransactionDto;
import br.com.armange.jpoc.spring.batch.cache.bd.domain.repository.TransactionCacheRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.XmlMappingException;

import java.io.IOException;
import java.util.List;

@Slf4j
public class TransactionXmlWriter extends StaxEventItemWriter<TransactionDto> {

    private final TransactionCacheRepository repository;

    public TransactionXmlWriter(
            final TransactionCacheRepository repository,
            final Marshaller marshaller,
            final String rootTagName,
            final Resource resource) {
        super.setMarshaller(marshaller);
        super.setRootTagName(rootTagName);
        super.setResource(resource);

        this.repository = repository;
    }

    @Override
    public void write(final List<? extends TransactionDto> items)
            throws XmlMappingException, IOException {
        super.write(items);

        final Iterable<TransactionDto> all = repository.findAll();

        log.info("hasNext(): {}", all.iterator().hasNext());
    }
}
