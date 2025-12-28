package org.jala.university.application.usecases;

import jakarta.transaction.Transactional;
import org.jala.university.application.dto.BeneficiaryDTO;
import org.jala.university.domain.entity.Account;
import org.jala.university.domain.entity.AccountBeneficiary;
import org.jala.university.domain.entity.AccountBeneficiaryId;
import org.jala.university.domain.entity.Beneficiary;
import org.jala.university.domain.repository.AccountBeneficiaryRepository;
import org.jala.university.domain.repository.AccountRepository;
import org.jala.university.domain.repository.BeneficiaryRepository;
import org.jala.university.domain.services.ValidationService;

import java.util.List;
import java.util.Optional;

public class BeneficiaryUseCases {
    private final BeneficiaryRepository beneficiaryRepository;
    private final AccountRepository accountRepository;
    private final AccountBeneficiaryRepository accountBeneficiaryRepository;
    private final ValidationService validationService;

    public BeneficiaryUseCases(
            BeneficiaryRepository beneficiaryRepository,
            AccountRepository accountRepository,
            AccountBeneficiaryRepository accountBeneficiaryRepository,
            ValidationService validationService) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.accountRepository = accountRepository;
        this.accountBeneficiaryRepository = accountBeneficiaryRepository;
        this.validationService = validationService;
    }

    public List<String> getDefaultCategories() {
        return List.of("Família", "Amigos", "Trabalho", "Outros");
    }

    public void addBeneficiary(BeneficiaryDTO beneficiaryDTO, Long accountId) {
        // Validar dados do beneficiário

        if (!validationService.validateBeneficiary(beneficiaryDTO)) {
            throw new IllegalArgumentException("Dados do beneficiário inválidos");
        }

        // Verificar se o beneficiário já existe
        if (beneficiaryRepository.existsBeneficiary(
                beneficiaryDTO.getDocument(),
                beneficiaryDTO.getAccountNumber(),
                accountId
        )) {
            throw new IllegalStateException("Beneficiário já cadastrado");
        }

        // Buscar a conta
        Account account = accountRepository.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Conta não encontrada");
        }

        // Criar o beneficiário
        Beneficiary beneficiary = Beneficiary.builder()
                .name(beneficiaryDTO.getName())
                .document(beneficiaryDTO.getDocument())
                .accountNumber(beneficiaryDTO.getAccountNumber())
                .bank(beneficiaryDTO.getBank())
                .category(beneficiaryDTO.getCategory()) //categories
                .build();

        // Salvar o beneficiário
        beneficiaryRepository.addBeneficiary(beneficiary);

        // Criar a relação entre conta e beneficiário
        AccountBeneficiaryId accountBeneficiaryId = new AccountBeneficiaryId(accountId, beneficiary.getId());
        AccountBeneficiary accountBeneficiary = AccountBeneficiary.builder()
                .id(accountBeneficiaryId)
                .account(account)
                .beneficiary(beneficiary)
                .favorite(beneficiaryDTO.isFavorite())
                .build();

        accountBeneficiaryRepository.save(accountBeneficiary);

    }

    public void editBeneficiary(Long beneficiaryId, BeneficiaryDTO updatedDTO) {
        // 1. Validação básica
        if (updatedDTO == null) {
            throw new IllegalArgumentException("Dados do beneficiário não podem ser nulos");
        }

        if (!validationService.validateBeneficiary(updatedDTO)) {
            throw new IllegalArgumentException("Dados do beneficiário inválidos");
        }

        // 2. Buscar o beneficiário pelo ID
        Optional<Beneficiary> beneficiaryOptional = beneficiaryRepository.findBeneficiaryById(beneficiaryId);
        Beneficiary beneficiaryToEdit = beneficiaryOptional
                .orElseThrow(() -> new IllegalArgumentException("Beneficiário não encontrado"));

        // 3. Verificar se outro beneficiário já tem os novos dados
        if (beneficiaryRepository.existsOtherBeneficiaryWithSameData(
                beneficiaryId,
                updatedDTO.getDocument(),
                updatedDTO.getAccountNumber()
        )) {
            throw new IllegalStateException("Documento ou conta já cadastrados para outro beneficiário");
        }

        // 4. Atualizar os campos
        beneficiaryToEdit.setName(updatedDTO.getName());
        beneficiaryToEdit.setDocument(updatedDTO.getDocument());
        beneficiaryToEdit.setAccountNumber(updatedDTO.getAccountNumber());
        beneficiaryToEdit.setBank(updatedDTO.getBank());
        beneficiaryToEdit.setCategory(updatedDTO.getCategory()); // categories

        // 5. Salvar as alterações
        beneficiaryRepository.updateBeneficiary(beneficiaryToEdit);
    }

    public List<Beneficiary> listBeneficiaries() {
        return beneficiaryRepository.listBeneficiaries();
    }

    public List<Beneficiary> listBeneficiariesByAccountId(Long accountId) {
        return beneficiaryRepository.findBeneficiariesByAccountId(accountId);
    }
    @Transactional
    public void removeBeneficiary(Long beneficiaryId) {
        beneficiaryRepository.removeBeneficiary(beneficiaryId);
    }


    public void setFavoriteBeneficiary(Long accountId, Long beneficiaryId, boolean favorite) {
        // 1. Validar parâmetros
        if (accountId == null || beneficiaryId == null) {
            throw new IllegalArgumentException("ID da conta e do beneficiário não podem ser nulos");
        }

        // 2. Verificar se a conta existe
        Account account = accountRepository.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Conta não encontrada");
        }

        // 3. Verificar se o beneficiário existe
        Optional<Beneficiary> beneficiaryOptional = beneficiaryRepository.findBeneficiaryById(beneficiaryId);
        Beneficiary beneficiary = beneficiaryOptional
                .orElseThrow(() -> new IllegalArgumentException("Beneficiário não encontrado"));

        // 4. Buscar ou criar a relação entre conta e beneficiário
        AccountBeneficiaryId id = new AccountBeneficiaryId(accountId, beneficiaryId);
        AccountBeneficiary accountBeneficiary = accountBeneficiaryRepository.findById(id);

        if (accountBeneficiary == null) {
            // Criar nova relação se não existir
            accountBeneficiary = AccountBeneficiary.builder()
                    .id(id)
                    .account(account)
                    .beneficiary(beneficiary)
                    .favorite(favorite)
                    .build();
        } else {
            // Atualizar relação existente
            accountBeneficiary.setFavorite(favorite);
        }

        // 5. Salvar a relação
        accountBeneficiaryRepository.save(accountBeneficiary);
    }
}
