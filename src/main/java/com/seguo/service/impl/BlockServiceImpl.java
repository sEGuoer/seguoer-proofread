package com.seguo.service.impl;

import com.seguo.dto.BlockDto;
import com.seguo.entity.Block;
import com.seguo.entity.Collection;
import com.seguo.entity.Lecture;
import com.seguo.repository.BlockRepository;
import com.seguo.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BlockServiceImpl implements BlockService {
    @Autowired
    BlockRepository blockRepository;

    @Override
    public void save(BlockDto blockDto) {
        Block block = new Block();

        if (blockDto.getId() != null) {
            block = blockRepository.findById(blockDto.getId()).get();
            block.setUpdatedAt(LocalDateTime.now());
        } else {
            block.setCreatedAt(LocalDateTime.now());
        }

        block.setContent(blockDto.getContent());
        block.setPublished(blockDto.isPublished());
        block.setSortOrder(blockDto.getSortOrder());
        block.setLecture(new Lecture(blockDto.getLecture_id()));
        block.setCollection(new Collection(blockDto.getCollection_id()));
        blockRepository.save(block);
    }

    @Override
    public Optional<Block> findById(Long id) {
        return blockRepository.findById(id);
    }

    @Override
    public void destroy(Long id) {
        blockRepository.deleteById(id);
    }

    @Override
    public void destroyAllById(List<Long> ids) {
        blockRepository.deleteAllById(ids);
    }
}

