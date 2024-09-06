package com.tiny.url.controllers;

import com.tiny.url.models.Url;
import com.tiny.url.repository.UrlRepository;
import com.tiny.url.services.TinyUrl;
import com.tiny.url.services.TinyUrlTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UrlControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private TinyUrl tinyUrlObj;

    @InjectMocks
    private UrlController urlController;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(urlController).build();
    }

    @Test
    public void testSaveUrl() throws Exception {
        String originalUrl = "http://www.example.com";
        Url newUrl = Url.builder().id("1").tinyUrl("abc123").originalUrl(originalUrl).build();

        when(tinyUrlObj.shortenUrl("http://www.example.com")).thenReturn(newUrl);

        mockMvc.perform(post("/v1/newTinyUrl")
                        .param("originalUrl", originalUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.tinyUrl").value("abc123"));
    }

    @Test
    public void testGetOriginalUrl() throws Exception {
        String tinyUrl = "abc123";
        Url url = Url.builder().id("2").tinyUrl(tinyUrl).originalUrl("http://www.example.com").build();

        when(urlRepository.findByTinyUrl(tinyUrl)).thenReturn(url);

        mockMvc.perform(get("/v1/getUrl")
                        .param("tinyUrl", tinyUrl))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value("http://www.example.com"))
                .andExpect(jsonPath("$.tinyUrl").value(tinyUrl));
    }
}
