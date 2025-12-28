package org.jala.university.application.service.impl.mocks.account;

import org.jala.university.application.dto.AccountDTO;
import org.jala.university.application.service.impl.mocks.user.UserMock;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountMock {
    public static Account createAValidAccount() {
        return Account.builder()
                .id(1L)
                .accountNumber("123456789")
                .accountType(AccountType.CHECKING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .agency("3829")
                .balance(BigDecimal.TEN)
                .user(UserMock.createAValidUser())
                .build();
    }

    public static AccountDTO createAValidAccountDTO() {
        return AccountDTO.builder()
                .id(1L)
                .accountNumber("123456789")
                .accountType(AccountType.CHECKING)
                .agency("3829")
                .balance(BigDecimal.TEN)
                .build();
    }
}
