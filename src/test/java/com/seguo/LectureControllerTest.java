package com.seguo;

import com.seguo.entity.*;
import com.seguo.repository.BlockRepository;
import com.seguo.repository.CollectionRepository;
import com.seguo.repository.LectureRepository;
import com.seguo.repository.SectionRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureMockMvc
class LectureControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    CollectionRepository collectionRepository;
    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    LectureRepository lectureRepository;
    @Autowired
    BlockRepository blockRepository;

    @Autowired
    Environment environment;

    @Test
    void showLectureWithContent() throws Exception {
        Collection collection = new Collection();
        collection.setTitle(UUID.randomUUID().toString());
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        Section section = new Section();
        section.setTitle(UUID.randomUUID().toString());
        section.setCollection(new Collection(collection.getId()));
        sectionRepository.save(section);

        String lectureContent = """
                ## block-1-title
                                                
                block-1-content
                                                
                ## block-2-title
                                                
                block-2-content
                                                
                ## block-3-title
                                                
                block-3-content
                """;
        Lecture lecture = new Lecture();
        lecture.setTitle(UUID.randomUUID().toString());
        lecture.setContent(lectureContent);
        lecture.setSection(new Section(section.getId()));
        lecture.setCollection(new Collection(collection.getId()));
        lectureRepository.save(lecture);

        mvc.perform(MockMvcRequestBuilders.get("/docs/lecture/" + lecture.getId()))
                // 左侧 doc-sidebar
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(section.getTitle())))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("href=\"#section-"+section.getId()+"\"")))
                // 中间 主内容区域
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(lectureContent)))
        // 右侧 aside-toc 内容是通过 tocbot 在前端解析之后自动生成的内容。暂时不测。
        ;

        Optional<Lecture> optionalLecture = lectureRepository.findById(lecture.getId());
        Assertions.assertTrue(optionalLecture.isPresent());
        lectureRepository.delete(lecture);

        Optional<Section> se = sectionRepository.findById(section.getId());
        Assertions.assertTrue(se.isPresent());
        sectionRepository.delete(se.get());

        Optional<Collection> co = collectionRepository.findById(collection.getId());
        Assertions.assertTrue(co.isPresent());
        collectionRepository.delete(co.get());
    }

    @Test
    void showLectureWithoutContentButHasBlocks() throws Exception {
        Collection collection = new Collection();
        collection.setTitle(UUID.randomUUID().toString());
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        Section section = new Section();
        section.setTitle(UUID.randomUUID().toString());
        section.setCollection(new Collection(collection.getId()));
        sectionRepository.save(section);

        Lecture lecture = new Lecture();
        lecture.setTitle(UUID.randomUUID().toString());
        lecture.setContent(null);
        lecture.setSection(new Section(section.getId()));
        lecture.setCollection(new Collection(collection.getId()));
        lectureRepository.save(lecture);

        String blockContent1 = """
                ## block-1-title
                                                
                block-1-content
                """;
        Block block1 = new Block();
        block1.setContent(blockContent1);
        block1.setLecture(lecture);
        block1.setCollection(collection);
        blockRepository.save(block1);

        String blockContent2 = """
                ## block-2-title
                                                
                block-2-content
                """;
        Block block2 = new Block();
        block2.setContent(blockContent2);
        block2.setLecture(lecture);
        block2.setCollection(collection);
        blockRepository.save(block2);


        mvc.perform(MockMvcRequestBuilders.get("/docs/lecture/" + lecture.getId()))
                // 左侧 doc-sidebar
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(section.getTitle())))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("href=\"#section-"+section.getId()+"\"")))
                // 中间 主内容区域
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString(blockContent1 + System.lineSeparator() + blockContent2)))
        // 右侧 aside-toc 内容是通过 tocbot 在前端解析之后自动生成的内容。暂时不测。
        ;


        Optional<Lecture> optionalLecture = lectureRepository.findById(lecture.getId());
        Assertions.assertTrue(optionalLecture.isPresent());
        List<Block> blocks = optionalLecture.get().getBlocks();
        Assertions.assertEquals(2, blocks.size());
        blockRepository.deleteAllById(blocks.stream().map(Block::getId).collect(Collectors.toList()));
        lectureRepository.delete(lecture);

        Optional<Section> se = sectionRepository.findById(section.getId());
        Assertions.assertTrue(se.isPresent());
        sectionRepository.delete(se.get());

        Optional<Collection> co = collectionRepository.findById(collection.getId());
        Assertions.assertTrue(co.isPresent());
        collectionRepository.delete(co.get());
    }
}

