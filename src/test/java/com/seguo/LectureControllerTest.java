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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

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

    @Test
    @WithMockUser(username="user",roles={"user"})
    void showNotContainsString() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20"))
                .andExpect(MockMvcResultMatchers.content().string(not(containsString("校对<i class=\"bi bi-spellcheck pl-1\"></i>"))))
        ;
    }

    @Test
    @WithMockUser(username="admin",roles={"admin"})
    void showContainsString() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("校对<i class=\"bi bi-spellcheck pl-1\"></i>")))
        ;
    }

    @Test
    void showProofreadReturn3xxWithoutLogin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20/proofread"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        ;
    }

    @Test
    @WithMockUser(username="not-admin",roles={"not-admin"})
    void showProofreadReturnForbiddenWithoutAdminRole() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20/proofread"))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
        ;
    }

    @Test
    @WithMockUser(username="admin",roles={"admin"})
    void showProofreadWithAdminRole() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/docs/lecture/20/proofread"))
                .andExpect(MockMvcResultMatchers.content().string(containsString("<i class=\"bi bi-arrow-return-left pl-1\"></i>返回正常页面")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("自动翻译<i class=\"bi bi-translate pl-1\"></i>")))
        ;
    }

    @Test
    @WithMockUser(username="admin",roles={"admin"})
    void autoTranslateBlocks() throws Exception {
        long lectureId = 20L;
        long blockId = 16L;

        Block block = blockRepository.findById(blockId).orElseThrow();
        String originContentTranslation = block.getContentTranslation();

        mvc.perform(MockMvcRequestBuilders.get("/docs/lecture/" + lectureId + "/auto-translate"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/docs/lecture/" + lectureId + "/proofread"))
        ;

        block = blockRepository.findById(blockId).orElseThrow();
        Assertions.assertNotNull(block.getContentTranslation());

        // rollback
        block.setContentTranslation(originContentTranslation);
        blockRepository.save(block);
    }

    @Test
    @WithMockUser(username="admin",roles={"admin"})
    void updateBlockWithContentColumn() throws Exception {
        long collectionId = 2L;
        long lectureId = 20L;
        long blockId = 16L;

        Block block = blockRepository.findById(blockId).orElseThrow();
        String originContent = block.getContent();
        String updatedContent = "content-column__updated";
        mvc.perform(MockMvcRequestBuilders.put("/docs/lecture/block/proofread/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", blockId + "")
                        .param("content", updatedContent)
                        .param("lecture_id", lectureId + "")
                        .param("collection_id", collectionId + "")
                )
                .andExpect(MockMvcResultMatchers.content().string(is("SUCCESS")))
        ;

        block = blockRepository.findById(blockId).orElseThrow();
        Assertions.assertEquals(updatedContent, block.getContent());

        // rollback
        block.setContent(originContent);
        blockRepository.save(block);
    }

    @Test
    @WithMockUser(username="admin",roles={"admin"})
    void updateBlockWithContentTranslationColumn() throws Exception {
        long collectionId = 2L;
        long lectureId = 20L;
        long blockId = 16L;

        Block block = blockRepository.findById(blockId).orElseThrow();
        String originContentTranslation = block.getContentTranslation();
        String updatedContentTranslation = "contentTranslation-column__updated";
        mvc.perform(MockMvcRequestBuilders.put("/docs/lecture/block/proofread/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", blockId + "")
                        .param("contentTranslation", updatedContentTranslation)
                        .param("lecture_id", lectureId + "")
                        .param("collection_id", collectionId + "")
                )
                .andExpect(MockMvcResultMatchers.content().string(is("SUCCESS")))
        ;

        block = blockRepository.findById(blockId).orElseThrow();
        Assertions.assertEquals(updatedContentTranslation, block.getContentTranslation());

        // rollback
        block.setContentTranslation(originContentTranslation);
        blockRepository.save(block);
    }
}


