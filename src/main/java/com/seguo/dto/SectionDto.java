package com.seguo.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SectionDto {

    Long id;
    @NotEmpty(groups = {CollectionDto.OnSave.class, CollectionDto.OnUpdate.class})
    String title;
    @Size(max = 512, groups = {CollectionDto.OnSave.class, CollectionDto.OnUpdate.class})
    String description;
    @Digits(integer = 5, fraction = 0)
    int sortOrder;
    @NotNull
    Long collection_id;
}
