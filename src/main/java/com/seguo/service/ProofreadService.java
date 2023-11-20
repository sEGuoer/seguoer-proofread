package com.seguo.service;

import com.seguo.entity.Collection;
import org.springframework.data.domain.Page;

public interface ProofreadService {
    Page<Collection> findAll(int pageNumber, int pageSize);

    void destroy(Long id);
}
