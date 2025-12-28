package org.jala.university.application.map;

import org.jala.university.application.dto.AccountDTO;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.Account;

/**
 * The AccountMapper class is responsible for mapping between the Account
 * entity and the AccountDTO data transfer object.
 *
 */
public class AccountMapper implements Mapper<Account, AccountDTO> {
    /**
     * Maps an Account entity to an AccountDTO object.
     *
     * @param input the Account entity to be mapped
     * @return the AccountDTO object with the mapped data
     */
    @Override
    public AccountDTO mapTo(Account input) {
        return AccountDTO.builder()
                .id(input.getId())
                .accountNumber(input.getAccountNumber())
                .accountType(input.getAccountType())
                .agency(input.getAgency())
                .balance(input.getBalance())
                .build();
    }

    /**
     * Maps an AccountDTO object to an Account entity.
     *
     * @param input the AccountDTO object to be mapped
     * @return the Account entity with the mapped data
     */
    @Override
    public Account mapFrom(AccountDTO input) {
        return Account.builder()
                .id(input.getId())
                .accountNumber(input.getAccountNumber())
                .accountType(input.getAccountType())
                .agency(input.getAgency())
                .balance(input.getBalance())
                .build();
    }
}
