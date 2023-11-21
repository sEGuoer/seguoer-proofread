package com.seguo.repository;

import com.seguo.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection,Long> {
    Optional<Collection> findFirstByTitle(String title);
}
