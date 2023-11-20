package com.seguo.repository;

import com.seguo.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection,Long> {
}
