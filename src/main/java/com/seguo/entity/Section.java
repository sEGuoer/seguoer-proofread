package com.seguo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    String slug;
    String title;
    String titleTranslation;
    String description;
    int sortOrder;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
    public Section(Long id) {
        this.id = id;
    }

    @ManyToOne(
            targetEntity = Collection.class,
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "collection_id", referencedColumnName = "id")
    Collection collection;

    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER)
    private List<Lecture> lectures = new ArrayList<>();
}
