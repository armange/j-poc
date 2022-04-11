package br.com.armange.jpoc.spring.batch.cache.bd.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.time.LocalDateTime;

@XmlRootElement(name = "transactionRecord")
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("Transaction")
@Data
public class TransactionDto implements Serializable {

    private int id;
    private String username;
    private int userId;
    private LocalDateTime transactionDate;
    private double amount;
}
