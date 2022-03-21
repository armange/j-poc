package br.com.armange.jpoc.spring.batch.processing.dto2dto;

import br.com.armange.jpoc.spring.batch.domain.dto.IdWrapperDto;
import br.com.armange.jpoc.spring.batch.domain.dto.IdentificationDto;
import org.springframework.batch.item.ItemProcessor;

public class Dto2DtoStep2 implements ItemProcessor<IdWrapperDto, IdWrapperDto> {

    @Override
    public IdWrapperDto process(final IdWrapperDto item) throws Exception {
        item.getIdentificationDto().getTransactionDto().setAmount(10);

        return item;
    }
}
