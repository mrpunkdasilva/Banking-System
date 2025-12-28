package org.jala.university.application.map;

import org.jala.university.application.dto.TransactionHistoryDTO;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.Transaction;

public class TransactionHistoryMapper implements Mapper<Transaction, TransactionHistoryDTO> {

    private final Long currentAccountId;

    public TransactionHistoryMapper(Long currentAccountId) {
        this.currentAccountId = currentAccountId;
    }

    @Override
    public TransactionHistoryDTO mapTo(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction entity cannot be null");
        }

        boolean debit = transaction.getSender().getId().equals(currentAccountId);
        String counterpartyName;
        if (debit) {
            counterpartyName = transaction.getReceiver().getUser().getName();
        } else {
            counterpartyName = transaction.getSender().getUser().getName();
        }

        return TransactionHistoryDTO.builder()
                .id(transaction.getId())
                .status(transaction.getStatus())
                .amount(transaction.getAmount())
                .date(transaction.getCreatedAt())
                .description(transaction.getDescription())
                .counterpartyName(counterpartyName)
                .debit(debit)
                .build();
    }

    @Override
    public Transaction mapFrom(TransactionHistoryDTO dto) {
        throw new UnsupportedOperationException("Converting from TransactionHistoryDTO to Transaction is not supported");
    }
}
