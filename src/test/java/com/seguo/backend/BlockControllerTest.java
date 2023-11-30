package com.seguo.backend;


import com.seguo.WithMockUserBaseTest;
import com.seguo.entity.*;
import com.seguo.repository.BlockRepository;
import com.seguo.repository.CollectionRepository;
import com.seguo.repository.LectureRepository;
import com.seguo.repository.SectionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.UUID;

class BlockControllerTest extends WithMockUserBaseTest {
    @Autowired
    CollectionRepository collectionRepository;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    BlockRepository blockRepository;

    @Test
    void store() throws Exception {
        Collection collection = new Collection();
        collection.setTitle(UUID.randomUUID().toString());
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        Section section = new Section();
        section.setTitle(UUID.randomUUID().toString());
        section.setCollection(collection);
        sectionRepository.save(section);

        Lecture lecture = new Lecture();
        lecture.setTitle(UUID.randomUUID().toString());
        lecture.setSection(section);
        lecture.setCollection(collection);
        lectureRepository.save(lecture);


        mvc.perform(MockMvcRequestBuilders
                        .post("/admin/blocks/store")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("content", UUID.randomUUID().toString())
                        .param("contentTranslation", "")
                        .param("sortOrder", "0")
                        .param("published", "1")
                        .param("lecture_id", lecture.getId().toString())
                        .param("collection_id", collection.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections/edit/" + collection.getId()))
        ;

        Optional<Block> ob = blockRepository.findFirstByLectureId(lecture.getId());
        Assertions.assertTrue(ob.isPresent());
        blockRepository.delete(ob.get());

        lectureRepository.delete(lecture);
        sectionRepository.delete(section);
        collectionRepository.delete(collection);
    }

    @Test
    void update() throws Exception {
        Collection collection = new Collection();
        collection.setTitle(UUID.randomUUID().toString());
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        Section section = new Section();
        section.setTitle(UUID.randomUUID().toString());
        section.setCollection(collection);
        sectionRepository.save(section);

        Lecture lecture = new Lecture();
        lecture.setTitle(UUID.randomUUID().toString());
        lecture.setSection(section);
        lecture.setCollection(collection);
        lectureRepository.save(lecture);

        Block block = new Block();
        block.setContent(UUID.randomUUID().toString());
        block.setSortOrder(0);
        block.setLecture(lecture);
        block.setCollection(collection);
        blockRepository.save(block);

        String illegalSortOrderValue = "100000";
        mvc.perform(MockMvcRequestBuilders.put("/admin/blocks/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", block.getId().toString())
                        .param("content", block.getContent())
                        .param("sortOrder", illegalSortOrderValue)
                        .param("lecture_id", lecture.getId().toString())
                        .param("collection_id", collection.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("block", "sortOrder", "Digits"))
        ;

        String contentUpdated = "description--updated";
        mvc.perform(MockMvcRequestBuilders.put("/admin/blocks/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", block.getId().toString())
                        .param("content", contentUpdated)
                        .param("sortOrder", "0")
                        .param("lecture_id", lecture.getId().toString())
                        .param("collection_id", collection.getId().toString())
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections/edit/" + collection.getId()))
        ;

        Block blockUpdated = blockRepository.findFirstByLectureId(lecture.getId()).orElseThrow();
        Assertions.assertEquals(contentUpdated, blockUpdated.getContent());

        blockRepository.delete(blockUpdated);
        lectureRepository.delete(lecture);
        sectionRepository.delete(section);
        collectionRepository.delete(collection);

        Assertions.assertTrue(blockRepository.findFirstByLectureId(lecture.getId()).isEmpty());
        Assertions.assertTrue(lectureRepository.findById(lecture.getId()).isEmpty());
        Assertions.assertTrue(sectionRepository.findById(section.getId()).isEmpty());
        Assertions.assertTrue(collectionRepository.findById(collection.getId()).isEmpty());
    }

    @Test
    void destroy() throws Exception {
        Collection collection = new Collection();
        collection.setTitle(UUID.randomUUID().toString());
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        Section section = new Section();
        section.setTitle(UUID.randomUUID().toString());
        section.setCollection(collection);
        sectionRepository.save(section);

        Lecture lecture = new Lecture();
        lecture.setTitle(UUID.randomUUID().toString());
        lecture.setSection(section);
        lecture.setCollection(collection);
        lectureRepository.save(lecture);

        Block block = new Block();
        block.setContent(UUID.randomUUID().toString());
        block.setSortOrder(0);
        block.setLecture(lecture);
        block.setCollection(collection);
        blockRepository.save(block);

        mvc.perform(MockMvcRequestBuilders.delete("/admin/blocks/destroy/" + block.getId()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections/edit/" + collection.getId()))
        ;

        Optional<Block> deletedBlock = blockRepository.findFirstByLectureId(lecture.getId());
        Assertions.assertTrue(deletedBlock.isEmpty());

        lectureRepository.delete(lecture);
        sectionRepository.delete(section);
        collectionRepository.delete(collection);

        Assertions.assertTrue(lectureRepository.findById(lecture.getId()).isEmpty());
        Assertions.assertTrue(sectionRepository.findById(section.getId()).isEmpty());
        Assertions.assertTrue(collectionRepository.findById(collection.getId()).isEmpty());
    }
}
