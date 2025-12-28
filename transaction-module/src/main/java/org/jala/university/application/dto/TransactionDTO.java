package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
public class TransactionDTO {
    private Long id;

    private BigDecimal amount;

    private Account sender;

    private Account receiver;

    private TransactionStatus status;

    private String description;

    private LocalDate transactionSchedule;

    private LocalDateTime createdAt;
}
