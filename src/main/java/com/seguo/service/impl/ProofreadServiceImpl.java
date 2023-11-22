package com.seguo.service.impl;

import com.seguo.dto.CollectionDto;
import com.seguo.entity.Collection;
import com.seguo.entity.User;
import com.seguo.repository.CollectionRepository;
import com.seguo.service.ProofreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProofreadServiceImpl implements ProofreadService {
    @Autowired
    CollectionRepository collectionRepository;

    @Override
    public Page<Collection> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
        return this.collectionRepository.findAll(pageable);
    }

    @Override
    public void destroy(Long id) {
        this.collectionRepository.deleteById(id);
    }

    @Override
    public void destroyAllById(List<Long> ids) {
        this.collectionRepository.deleteAllById(ids);
    }

    @Override
    public void save(CollectionDto collectionDto) {
        Collection collection = new Collection();

        if (collectionDto.getId() != null) {
            collection = collectionRepository.findById(collectionDto.getId()).get();
            collection.setUpdatedAt(LocalDateTime.now());
        } else {
            collection.setCreatedAt(LocalDateTime.now());
        }

        collection.setTitle(collectionDto.getTitle());
        collection.setSlug(collectionDto.getSlug());
        collection.setType(collectionDto.getType());
        collection.setDescription(collectionDto.getDescription());
        collection.setPublished(collectionDto.isPublished());
        collection.setCover(collectionDto.getCover());
        collection.setUser(new User(collectionDto.getUser_id()));
        collectionRepository.save(collection);
    }

    @Override
    public Optional<Collection> findById(Long id) {
        return this.collectionRepository.findById(id);

    }

    @Override
    public Page<Collection> findAllDocs(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
        return this.collectionRepository.findAllByType("doc", pageable);
    }
}
