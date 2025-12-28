package org.jala.university.domain.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "beneficiaries")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String document;
    private String accountNumber;
    private String bank;
    private String category;

    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AccountBeneficiary> accountBeneficiaries;
}
