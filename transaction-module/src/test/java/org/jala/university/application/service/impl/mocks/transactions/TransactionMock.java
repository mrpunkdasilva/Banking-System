package org.jala.university.application.service.impl.mocks.transactions;

import org.jala.university.application.dto.TransactionDTO;
import org.jala.university.application.service.impl.mocks.account.AccountMock;
import org.jala.university.domain.entity.Transaction;
import org.jala.university.domain.entity.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionMock {

    public static Transaction createAValidTransaction() {
        return Transaction.builder()
                .id(1L)
                .amount(BigDecimal.ONE)
                .description("description")
                .createdAt(LocalDateTime.now())
                .receiver(AccountMock.createAValidAccount())
                .sender(AccountMock.createAValidAccount())
                .status(TransactionStatus.COMPLETED)
                .build();
    }

    public static TransactionDTO createAValidTransactionDTO() {
        return TransactionDTO.builder()
                .id(1L)
                .amount(BigDecimal.ONE)
                .description("description")
                .createdAt(LocalDateTime.now())
                .receiver(AccountMock.createAValidAccount())
                .sender(AccountMock.createAValidAccount())
                .status(TransactionStatus.COMPLETED)
                .build();
    }
}
