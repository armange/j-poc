package br.com.armange.jpoc.spring.batch.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("restriction")
@XmlRootElement(name = "transactionRecord")
@Data
@NoArgsConstructor
public class TransactionDto implements Serializable {

    private String username;
    private int userId;
    private LocalDateTime transactionDate;
    private double amount;

    public TransactionDto(final int userId) {
        setUserId(userId);
    }

    @Override
    public String toString() {
        return "Transaction [username=" + username + ", userId=" + userId
                + ", transactionDate=" + transactionDate + ", amount=" + amount
                + "]";
    }
}
