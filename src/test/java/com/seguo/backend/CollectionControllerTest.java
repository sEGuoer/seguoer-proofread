package com.seguo.backend;


import com.seguo.entity.Collection;
import com.seguo.entity.User;
import com.seguo.repository.CollectionRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails(userDetailsServiceBeanName = "jpaUserDetailsService", value = "admin@example.com")
class CollectionControllerTest {
    @Autowired
    public MockMvc mvc;
    @Test
    void index() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/admin/collections"))
                .andExpect(MockMvcResultMatchers.view().name("backend/collection/index"))
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Collection 管理")))
        ;
    }

    @Test
    void deleteById(@Autowired CollectionRepository collectionRepository) throws Exception {
        Collection collection = new Collection();
        collection.setTitle(UUID.randomUUID().toString());
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setUser(new User(1L));

        collectionRepository.save(collection);

        mvc.perform(MockMvcRequestBuilders.delete("/admin/collections/destroy/" + collection.getId()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"))
        ;

        Optional<Collection> byId = collectionRepository.findById(collection.getId());
        Assertions.assertTrue(byId.isEmpty());
    }

    @Test
    void batchDelete(@Autowired CollectionRepository collectionRepository) throws Exception {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Collection collection = new Collection();
            collection.setTitle(UUID.randomUUID().toString());
            collection.setSlug(UUID.randomUUID().toString());
            collection.setType("doc");
            collection.setUser(new User(1L));

            Collection c = collectionRepository.save(collection);
            ids.add(c.getId());
        }

        mvc.perform(MockMvcRequestBuilders.delete("/admin/collections/destroy")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("ids[]", ids.get(0).toString())
                        .param("ids[]", ids.get(1).toString())
                )
                .andExpect(MockMvcResultMatchers.content().string("DONE"))
        ;

        List<Collection> allById = collectionRepository.findAllById(ids);
        Assertions.assertTrue(allById.isEmpty());
    }

    @Test
    void store(@Autowired CollectionRepository collectionRepository) throws Exception {
        String title = "title-" + UUID.randomUUID();
        mvc.perform(MockMvcRequestBuilders
                        .post("/admin/collections/store")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("title", title)
                        .param("slug", UUID.randomUUID().toString())
                        .param("type", "doc")
                        .param("description", "content-" + UUID.randomUUID())
                        .param("user_id", "1")
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"))
        ;

        Optional<Collection> co = collectionRepository.findFirstByTitle(title);
        Assertions.assertTrue(co.isPresent());

        collectionRepository.delete(co.get());
    }

    @Test
    void storeWithCoverImage(@Autowired CollectionRepository collectionRepository, @Autowired Environment env) throws Exception {
        String title = "title-" + UUID.randomUUID();
        MockMultipartFile coverFile = new MockMultipartFile("coverFile", "cover.png", MediaType.IMAGE_PNG_VALUE, new byte[] { 1, 2, 3 });
        mvc.perform(MockMvcRequestBuilders
                        .multipart("/admin/collections/store")
                        //.contentType(MediaType.MULTIPART_FORM_DATA)
                        .file(coverFile)
                        .param("id", "")
                        .param("title", title)
                        .param("slug", UUID.randomUUID().toString())
                        .param("type", "doc")
                        .param("description", "content-" + UUID.randomUUID())
                        .param("user_id", "1")
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"))
        ;

        Optional<Collection> co = collectionRepository.findFirstByTitle(title);
        Assertions.assertTrue(co.isPresent());

        String cover = co.get().getCover();
        File coverOnDisk = new File(env.getProperty("custom.upload.base-path") + cover);
        Assertions.assertTrue(Files.exists(coverOnDisk.toPath()));
        Assertions.assertTrue(coverOnDisk.delete());

        collectionRepository.delete(co.get());
    }

    @Test
    void update(@Autowired CollectionRepository collectionRepository) throws Exception {
        String title = "title-" + UUID.randomUUID();
        mvc.perform(MockMvcRequestBuilders.post("/admin/collections/store")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("title", title)
                        .param("slug", UUID.randomUUID().toString())
                        .param("type", "doc")
                        .param("description", "content-" + UUID.randomUUID())
                        .param("user_id", "1")
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"))
        ;
        Optional<Collection> co = collectionRepository.findFirstByTitle(title);
        Assertions.assertTrue(co.isPresent());
        Collection collection = co.get();

        String descriptionUpdated = "description--updated";
        mvc.perform(MockMvcRequestBuilders.put("/admin/collections/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", collection.getId().toString())
                        .param("title", collection.getTitle())
                        .param("slug", UUID.randomUUID().toString())
                        .param("type", collection.getType())
                        .param("description", descriptionUpdated)
                        .param("user_id", collection.getUser().getId().toString())
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/collections"))
        ;

        Collection collectionUpdated = collectionRepository.findFirstByTitle(title).get();
        Assertions.assertEquals(descriptionUpdated, collectionUpdated.getDescription());

        collectionRepository.delete(co.get());
    }

    @Test
    void togglePublished(@Autowired CollectionRepository collectionRepository) throws Exception {
        boolean published = false;
        Collection collection = new Collection();
        collection.setTitle(UUID.randomUUID().toString());
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setPublished(published);
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        mvc.perform(MockMvcRequestBuilders.post("/admin/collections/togglePublished/" + collection.getId()))
                .andExpect(MockMvcResultMatchers.content().string("SUCCESS"))
        ;

        Optional<Collection> byId = collectionRepository.findById(collection.getId());
        Assertions.assertTrue(byId.isPresent());
        Assertions.assertEquals(byId.get().isPublished(), !published);
    }

    @Test
    void storeWithCustomUniqueValidator(@Autowired CollectionRepository collectionRepository) throws Exception {
        String title = "title-" + UUID.randomUUID();
        String slug = "slug-" + UUID.randomUUID();

        Collection collection = new Collection();
        collection.setTitle(title);
        collection.setSlug(UUID.randomUUID().toString());
        collection.setType("doc");
        collection.setSlug(slug);
        collection.setUser(new User(1L));
        collectionRepository.save(collection);

        mvc.perform(MockMvcRequestBuilders
                        .post("/admin/collections/store")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", "")
                        .param("title", title)
                        .param("slug", slug)
                        .param("type", "doc")
                        .param("user_id", "1")
                )
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("collection", "slug", "CustomUnique"))
        ;

        mvc.perform(MockMvcRequestBuilders
                        .put("/admin/collections/update")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("id", collection.getId().toString())
                        .param("title", title + "--updated")
                        .param("slug", slug)
                        .param("type", "doc")
                        .param("user_id", "1")
                )
                .andExpect(MockMvcResultMatchers.redirectedUrl("/backend/collections"))
        ;

        Optional<Collection> co = collectionRepository.findFirstByTitle(title + "--updated");
        Assertions.assertTrue(co.isPresent());

        collectionRepository.delete(co.get());
    }
}