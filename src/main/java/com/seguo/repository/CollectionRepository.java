package com.seguo.repository;

import com.seguo.entity.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection,Long> {
    Optional<Collection> findFirstByTitle(String title);

    Page<Collection> findAllByType(String type, Pageable pageable);

    @Modifying
    @Query(value = """
            UPDATE Collection co\s
            SET co.published =\s
              CASE co.published\s
                WHEN TRUE THEN FALSE
                ELSE TRUE
              END
            WHERE co.id = :id
            """)
    void togglePublished(Long id);
}
