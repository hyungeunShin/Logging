package com.example.logging.controller;

import com.example.logging.service.ShortenUrlService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ShortenUrlController.class)
class ShortenUrlControllerTest {
    @MockitoBean
    private ShortenUrlService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("원래의 URL로 리다이렉트 되어야한다")
    void redirectTest() throws Exception {
        String expectedOriginalUrl = "https://www.naver.com";

        Mockito.when(service.findOriginalUrlByShortenUrlKey(Mockito.any())).thenReturn(expectedOriginalUrl);

        mockMvc.perform(MockMvcRequestBuilders.get("/any-key"))
               .andExpect(MockMvcResultMatchers.status().isMovedPermanently())
               .andExpect(MockMvcResultMatchers.header().string("Location", expectedOriginalUrl));
    }
}