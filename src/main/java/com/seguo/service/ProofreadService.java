package com.seguo.service;

import com.seguo.dto.CollectionDto;
import com.seguo.entity.Collection;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProofreadService {
    Page<Collection> findAll(int pageNumber, int pageSize);

    void destroy(Long id);

    void destroyAllById(List<Long> ids);

    void save(CollectionDto collectionDto);

    Optional<Collection> findById(Long id);
    Page<Collection> findAllPublishedDocs(int pageNumber, int pageSize);

    void togglePublished(Long id);
}
