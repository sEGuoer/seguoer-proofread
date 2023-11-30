package com.seguo.service;

import com.seguo.dto.LectureDto;
import com.seguo.entity.Lecture;

import java.util.List;
import java.util.Optional;

public interface LectureService {
    Lecture save(LectureDto lectureDto);

    Optional<Lecture> findById(Long id);

    void destroy(Long id);

    void destroyAllById(List<Long> ids);

    void saveBlocks(Long lectureId, LectureDto lectureDto);
}
