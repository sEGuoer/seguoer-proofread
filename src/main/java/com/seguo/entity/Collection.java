package com.seguo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@DynamicUpdate
public class Collection {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    String title;
    String titleTranslation;
    @Column(unique = true)
    String slug;
    String type;
    String content;
    String video;
    long duration;
    String cover;
    String description;
    boolean published;
    boolean free;
    Double price;
    int viewCount;
    String seoTitle;
    String seoDescription;
    boolean completed;
    boolean proofread;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    @ManyToOne(
            targetEntity = User.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;
}
