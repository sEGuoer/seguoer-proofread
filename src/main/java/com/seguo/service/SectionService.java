package com.seguo.service;

import com.seguo.dto.SectionDto;
import com.seguo.entity.Section;

import java.util.Optional;

public interface SectionService {
    void save(SectionDto sectionDto);

    Optional<Section> findById(Long id);

    void destroy(Long id);
}
