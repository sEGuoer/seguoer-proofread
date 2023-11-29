package com.seguo.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureDto {
    Long id;

    @NotEmpty
    String title;

    String content;

    boolean published;

    boolean free;

    boolean requiresLogin;

    @Digits(integer = 5, fraction = 0)
    int sortOrder;

    @Size(max = 512)
    String description;

    @NotNull
    Long section_id;

    @NotNull
    Long collection_id;
}
