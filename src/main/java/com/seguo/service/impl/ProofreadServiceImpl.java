package com.seguo.service.impl;

import com.seguo.entity.Collection;
import com.seguo.repository.CollectionRepository;
import com.seguo.service.ProofreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProofreadServiceImpl implements ProofreadService {
    @Autowired
    CollectionRepository collectionRepository;

    @Override
    public Page<Collection> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
        return this.collectionRepository.findAll(pageable);
    }
}
