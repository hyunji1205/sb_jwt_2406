package com.example.jwt.domain.article;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.awt.*;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ArticleControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("GET /articles")
    void t1() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/articles")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.articles[0].id").exists());
    }


    @Test
    @DisplayName("GET /articles/1")
    void t2() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        get("/api/v1/articles/1")
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-1"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article.id").value(1));
    }

    @Test
    @DisplayName("POST /articles/1")
    @WithUserDetails("user1")
    void t3() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/v1/articles")
                                .content("""
                                        {
                                            "subject": "제목 new",
                                            "content": "내용 new"
                                        }
                                        """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-3"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article").exists());
    }

    @Test
    @DisplayName("POST /articles/2")
    @WithUserDetails("admin")
    void t4() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        patch("/api/v1/articles/2")
                                .content("""
                                        {
                                            "subject": "제목 2222 !!!",
                                            "content": "내용 2222 !!!"
                                        }
                                        """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.resultCode").value("S-4"))
                .andExpect(jsonPath("$.msg").exists())
                .andExpect(jsonPath("$.data.article.id").value(2))
                .andExpect(jsonPath("$.data.article.subject").value("제목 2222 !!!"))
                .andExpect(jsonPath("$.data.article.content").value("내용 2222 !!!"));

    }
}