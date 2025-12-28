package org.jala.university.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountBeneficiaryId implements Serializable {
    private Long accountId;
    private Long beneficiaryId;
}
