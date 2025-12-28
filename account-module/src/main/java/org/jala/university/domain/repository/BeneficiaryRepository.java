package org.jala.university.domain.repository;

import org.jala.university.domain.entity.Beneficiary;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryRepository {
    void addBeneficiary(Beneficiary beneficiary);

    void removeBeneficiary(Long beneficiaryId);

    List<Beneficiary> listBeneficiaries();

    boolean existsBeneficiary(String document, String accountNumber, Long accountId); // Updated method signature

    Optional<Beneficiary> findBeneficiaryById(Long beneficiaryId);

    void updateBeneficiary(Beneficiary beneficiary);

    boolean existsOtherBeneficiaryWithSameData(Long currentBeneficiaryId, String document, String accountNumber);

    List<Beneficiary> findBeneficiariesByAccountId(Long accountId);
}
