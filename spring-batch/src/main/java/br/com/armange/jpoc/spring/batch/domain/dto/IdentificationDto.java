package br.com.armange.jpoc.spring.batch.domain.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class IdentificationDto {
    private final TransactionDto transactionDto;

    @Override
    public String toString() {
        return transactionDto.toString();
    }
}
