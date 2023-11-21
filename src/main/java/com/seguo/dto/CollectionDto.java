package com.seguo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionDto {

    Long id;
    @NotEmpty
    String title;
    String slug;
    @Pattern(regexp = "doc|video")
    String type;
    @Size(max = 512)
    String description;
    boolean published;
    String cover;
    Long user_id;
}
