package com.seguo.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockDto {
    Long id;

    String content;

    String contentTranslation;

    @Digits(integer = 5, fraction = 0)
    int sortOrder;

    boolean published;

    @NotNull
    Long lecture_id;

    @NotNull
    Long collection_id;
}
