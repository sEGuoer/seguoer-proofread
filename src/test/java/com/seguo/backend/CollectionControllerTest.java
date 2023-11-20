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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"admin"})
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
}