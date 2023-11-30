package com.seguo.repository;

import com.seguo.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    Optional<Block> findFirstByLectureId(Long lectureId);
}