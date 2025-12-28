package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.Transaction;

import java.util.List;

public interface TransactionsRepository extends Repository<Transaction, Long> {
    List<Transaction> findAllByUserId(Long userId);
}
