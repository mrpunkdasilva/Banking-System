package org.jala.university.application.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BeneficiaryDTO {
    private String name;
    private String document;
    private String accountNumber;
    private String bank;
    private boolean favorite;
    private String category;
}
