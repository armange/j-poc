package br.com.armange.jpoc.spring.batch.writting.stateful;

import br.com.armange.jpoc.spring.batch.domain.dto.IdWrapperDto;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class Dto2DtoItemWriter implements ItemWriter<IdWrapperDto> {

    @Override
    public void write(List<? extends IdWrapperDto> items) throws Exception {
        items.forEach(System.out::println);
    }
}
