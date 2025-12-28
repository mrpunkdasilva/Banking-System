package org.jala.university.application.map;

import org.jala.university.application.dto.TransactionDTO;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.Transaction;

public class TransactionMapper implements Mapper<Transaction, TransactionDTO> {
    @Override
    public TransactionDTO mapTo(Transaction input) {
        return TransactionDTO.builder()
                .id(input.getId())
                .description(input.getDescription())
                .createdAt(input.getCreatedAt())
                .status(input.getStatus())
                .amount(input.getAmount())
                .receiver(input.getReceiver())
                .sender(input.getSender())
                .transactionSchedule(input.getTransactionSchedule())
                .build();
    }

    @Override
    public Transaction mapFrom(TransactionDTO input) {
        return Transaction.builder()
                .description(input.getDescription())
                .createdAt(input.getCreatedAt())
                .status(input.getStatus())
                .amount(input.getAmount())
                .receiver(input.getReceiver())
                .sender(input.getSender())
                .transactionSchedule(input.getTransactionSchedule())
                .build();
    }
}
