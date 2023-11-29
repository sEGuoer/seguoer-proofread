package com.seguo.service;

import com.seguo.dto.LectureDto;
import com.seguo.entity.Lecture;

import java.util.Optional;

public interface LectureService {
    void save(LectureDto lectureDto);

    Optional<Lecture> findById(Long id);

    void destroy(Long id);
}
