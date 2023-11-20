package com.seguo.service;

import com.seguo.entity.Collection;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProofreadService {
    Page<Collection> findAll(int pageNumber, int pageSize);

    void destroy(Long id);

    void destroyAllById(List<Long> ids);
}
