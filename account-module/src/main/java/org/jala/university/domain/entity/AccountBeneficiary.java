package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_beneficiaries")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountBeneficiary {
    @EmbeddedId
    private AccountBeneficiaryId id;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("beneficiaryId")
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;

    @Column(name = "is_favorite")
    private Boolean favorite;
}
