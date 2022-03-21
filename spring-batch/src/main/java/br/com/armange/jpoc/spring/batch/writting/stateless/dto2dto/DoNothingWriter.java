package br.com.armange.jpoc.spring.batch.writting.stateless.dto2dto;

import br.com.armange.jpoc.spring.batch.domain.dto.IdWrapperDto;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class DoNothingWriter implements ItemWriter<IdWrapperDto> {

    @Override
    public void write(List<? extends IdWrapperDto> items) throws Exception {
        items.parallelStream().forEach(System.out::println);
    }
}
