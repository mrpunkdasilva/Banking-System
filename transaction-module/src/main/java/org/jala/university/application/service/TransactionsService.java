package org.jala.university.application.service;

import org.jala.university.application.dto.TransactionDTO;
import org.jala.university.domain.entity.Transaction;

import java.util.List;

public interface TransactionsService {
    List<TransactionDTO> findAll(Long userId);

    List<TransactionDTO> findAll();

    TransactionDTO findById(Long id);

    boolean save(TransactionDTO transaction);

    boolean delete(Long id);
}
