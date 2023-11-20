package com.seguo.backend;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
}