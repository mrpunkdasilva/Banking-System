package org.jala.university.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
@Getter
public class SampleEntityDto {

    private UUID id;
    private String name;
}
