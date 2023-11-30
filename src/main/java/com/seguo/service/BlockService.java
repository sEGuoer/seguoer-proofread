package com.seguo.service;

import com.seguo.dto.BlockDto;
import com.seguo.entity.Block;

import java.util.List;
import java.util.Optional;

public interface BlockService {
    void save(BlockDto blockDto);

    Optional<Block> findById(Long id);

    void destroy(Long id);

    void destroyAllById(List<Long> ids);
}
