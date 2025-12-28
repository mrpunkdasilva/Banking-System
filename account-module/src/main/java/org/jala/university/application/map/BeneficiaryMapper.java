package org.jala.university.application.map;

import org.jala.university.application.dto.BeneficiaryDTO;
import org.jala.university.commons.application.mapper.Mapper;
import org.jala.university.domain.entity.Beneficiary;

public class BeneficiaryMapper implements Mapper<Beneficiary, BeneficiaryDTO> {
    @Override
    public BeneficiaryDTO mapTo(Beneficiary input) {
        return BeneficiaryDTO.builder()
                .accountNumber(input.getAccountNumber())
                .bank(input.getBank())
                .name(input.getName())
                .document(input.getDocument())
                .favorite(true)
                .build();
    }

    @Override
    public Beneficiary mapFrom(BeneficiaryDTO input) {
        return Beneficiary.builder()
                .accountNumber(input.getAccountNumber())
                .bank(input.getBank())
                .name(input.getName())
                .document(input.getDocument())
                .build();
    }
}
