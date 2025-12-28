package org.jala.university.domain.repository;

import org.jala.university.domain.entity.AccountBeneficiary;
import org.jala.university.domain.entity.AccountBeneficiaryId;

public interface AccountBeneficiaryRepository {
    AccountBeneficiary findById(AccountBeneficiaryId id);
    void save(AccountBeneficiary accountBeneficiary);
    void delete(AccountBeneficiaryId id);
}
