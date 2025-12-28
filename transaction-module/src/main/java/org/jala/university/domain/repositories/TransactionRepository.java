package org.jala.university.domain.repositories;

import org.jala.university.domain.entity.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Busca transações onde a conta é origem OU destino
     */
    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :accountId OR t.sender.id = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId, Pageable pageable);

    /**
     * Busca todas as transações de uma conta ordenadas por data
     */
    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :accountId OR t.receiver.id = :accountId ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(@Param("accountId") Long accountId);

    /**
     * Busca transações por período
     */
    @Query("SELECT t FROM Transaction t WHERE (t.sender.id = :accountId OR t.receiver.id = :accountId) AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountIdAndCreatedAtBetween(
        @Param("accountId") Long accountId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * Busca transações de saída (onde a conta é origem)
     */
    @Query("SELECT t FROM Transaction t WHERE t.sender.id = :accountId ORDER BY t.createdAt DESC")
    List<Transaction> findOutgoingTransactions(@Param("accountId") Long accountId);

    /**
     * Busca transações de entrada (onde a conta é destino)
     */
    @Query("SELECT t FROM Transaction t WHERE t.receiver.id = :accountId ORDER BY t.createdAt DESC")
    List<Transaction> findIncomingTransactions(@Param("accountId") Long accountId);

    /**
     * Conta total de transações de uma conta
     */
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.sender.id = :accountId OR t.receiver.id = :accountId")
    Long countByAccountId(@Param("accountId") Long accountId);
}