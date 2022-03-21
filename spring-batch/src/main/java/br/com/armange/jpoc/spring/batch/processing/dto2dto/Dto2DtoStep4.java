package br.com.armange.jpoc.spring.batch.processing.dto2dto;

import br.com.armange.jpoc.spring.batch.domain.dto.IdWrapperDto;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class Dto2DtoStep4 implements ItemProcessor<IdWrapperDto, IdWrapperDto> {

    @Override
    public IdWrapperDto process(final IdWrapperDto item) throws Exception {
        item.getIdentificationDto().getTransactionDto().setTransactionDate(LocalDateTime.now());

        return item;
    }
}
