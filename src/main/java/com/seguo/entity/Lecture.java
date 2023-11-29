package com.seguo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DynamicUpdate
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    String titleTranslation;
    @Column(unique = true)
    String slug;
    String content;
    String video;
    String videoId;
    long duration;
    String description;
    String descriptionTranslation;
    int sortOrder;
    boolean published;
    boolean free;
    boolean requiresLogin;
    String cover;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    public Lecture(Long id) {
        this.id = id;
    }

    @ManyToOne(
            targetEntity = Section.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    Section section;

    @ManyToOne(
            targetEntity = Collection.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "collection_id", referencedColumnName = "id")
    Collection collection;
}
