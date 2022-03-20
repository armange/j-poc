package br.com.armange.jpoc.spring.batch.configuration.domain.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;

@SuppressWarnings("restriction")
@XmlRootElement(name = "transactionRecord")
@Data
public class TransactionDto {
    private String username;
    private int userId;
    private LocalDateTime transactionDate;
    private double amount;

    @Override
    public String toString() {
        return "Transaction [username=" + username + ", userId=" + userId
                + ", transactionDate=" + transactionDate + ", amount=" + amount
                + "]";
    }
}
