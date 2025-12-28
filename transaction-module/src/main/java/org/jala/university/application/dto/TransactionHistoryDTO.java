package org.jala.university.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jala.university.domain.entity.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for transaction history entries.
 * Contains summarized information for displaying transactions in a history list.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryDTO {
    private Long id;
    private TransactionStatus status;
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;
    private String counterpartyName;
    private boolean debit; // true = Débito, false = Crédito
}
