package com.seguo.service.impl;

import com.seguo.dto.LectureDto;
import com.seguo.entity.Collection;
import com.seguo.entity.Lecture;
import com.seguo.entity.Section;
import com.seguo.repository.LectureRepository;
import com.seguo.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LectureServiceImpl implements LectureService {
    @Autowired
    LectureRepository lectureRepository;

    @Override
    public void save(LectureDto lectureDto) {
        Lecture lecture = new Lecture();

        lecture.setTitle(lectureDto.getTitle());
        lecture.setContent(lectureDto.getContent());
        lecture.setPublished(lectureDto.isPublished());
        lecture.setFree(lectureDto.isFree());
        lecture.setRequiresLogin(lectureDto.isRequiresLogin());
        lecture.setSortOrder(lectureDto.getSortOrder());
        lecture.setDescription(lectureDto.getDescription());
        lecture.setSection(new Section(lectureDto.getSection_id()));
        lecture.setCollection(new Collection(lectureDto.getCollection_id()));
        lecture.setCreatedAt(LocalDateTime.now());
        lectureRepository.save(lecture);
    }
}

