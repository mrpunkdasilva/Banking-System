package org.jala.university.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.jala.university.commons.domain.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
public final class SampleEntity implements BaseEntity<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String name;

    @CreatedDate
    private Date created;

    @LastModifiedDate
    private Date updated;

    @Override
    public UUID getId() {
        return id;
    }
}
