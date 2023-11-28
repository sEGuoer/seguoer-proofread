package com.seguo.dto;

import com.seguo.annotation.CustomUnique;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CollectionDto {

    Long id;
    @NotEmpty(groups = {OnSave.class, OnUpdate.class})
    String title;
    @CustomUnique(groups = OnSave.class)
    String slug;
    @Pattern(regexp = "doc|video", groups = {OnSave.class, OnUpdate.class})
    String type;
    @Size(max = 512, groups = {OnSave.class, OnUpdate.class})
    String description;
    boolean published;
    String cover;
    Long user_id;


    public interface OnSave {
    }
    public interface OnUpdate {
    }
}
