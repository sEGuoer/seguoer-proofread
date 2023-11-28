package com.seguo.repository;

import com.seguo.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findFirstByTitle(String title);
}