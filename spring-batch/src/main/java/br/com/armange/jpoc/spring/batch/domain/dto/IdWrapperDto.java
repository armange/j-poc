package br.com.armange.jpoc.spring.batch.domain.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class IdWrapperDto {

    private final IdentificationDto identificationDto;

    @Override
    public String toString() {
        return identificationDto.toString();
    }
}
