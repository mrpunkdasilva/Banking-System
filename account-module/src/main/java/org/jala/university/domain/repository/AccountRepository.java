package org.jala.university.domain.repository;

import org.jala.university.commons.domain.Repository;
import org.jala.university.domain.entity.Account;

public interface AccountRepository extends Repository<Account, Long> {
    Account findAccountByUserId(Long userId);
    Account findByAccountId(Long accountId);
    Account findByAccountNumber(String accountNumber);
}
