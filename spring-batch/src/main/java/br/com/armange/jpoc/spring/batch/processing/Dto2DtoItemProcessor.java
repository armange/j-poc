package br.com.armange.jpoc.spring.batch.processing;

import br.com.armange.jpoc.spring.batch.domain.dto.IdWrapperDto;
import br.com.armange.jpoc.spring.batch.domain.dto.IdentificationDto;
import org.springframework.batch.item.ItemProcessor;

public class Dto2DtoItemProcessor implements ItemProcessor<IdentificationDto, IdWrapperDto> {

    @Override
    public IdWrapperDto process(final IdentificationDto item) throws Exception {
        return new IdWrapperDto(item);
    }
}
