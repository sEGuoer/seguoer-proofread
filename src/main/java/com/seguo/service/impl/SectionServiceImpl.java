package com.seguo.service.impl;

import com.seguo.dto.SectionDto;
import com.seguo.entity.Collection;
import com.seguo.entity.Section;
import com.seguo.repository.SectionRepository;
import com.seguo.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {
    @Autowired
    SectionRepository sectionRepository;
    @Override
    public void save(SectionDto sectionDto) {
        Section section = new Section();

        if (sectionDto.getId() != null) {
            section = sectionRepository.findById(sectionDto.getId()).get();
            section.setUpdatedAt(LocalDateTime.now());
        } else {
            section.setCreatedAt(LocalDateTime.now());
        }

        section.setTitle(sectionDto.getTitle());
        section.setSortOrder(sectionDto.getSortOrder());
        section.setDescription(sectionDto.getDescription());
        section.setCollection(new Collection(sectionDto.getCollection_id()));
        sectionRepository.save(section);
    }

    @Override
    public Optional<Section> findById(Long id) {
        return sectionRepository.findById(id);
    }
}
