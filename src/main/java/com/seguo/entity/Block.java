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
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String content;
    String contentTranslation;
    int sortOrder;
    boolean published;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;

    public Block(Long id) {
        this.id = id;
    }

    @ManyToOne(
            targetEntity = Lecture.class,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "lecture_id", referencedColumnName = "id")
    Lecture lecture;

    @ManyToOne(
            targetEntity = Collection.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "collection_id", referencedColumnName = "id")
    Collection collection;
}

