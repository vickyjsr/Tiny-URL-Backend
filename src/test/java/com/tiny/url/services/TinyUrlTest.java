package com.tiny.url.services;

import com.tiny.url.helpers.CodeGenerator;
import com.tiny.url.models.Url;
import com.tiny.url.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TinyUrlTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private CodeGenerator codeGenerator;

    @InjectMocks
    private TinyUrl tinyUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShortenUrlWhenUrlExists() {
        String originalUrl = "http://example.com";
        Url existingUrl = Url.builder().tinyUrl("abc123").originalUrl(originalUrl).build();

        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(existingUrl);

        Url result = tinyUrl.shortenUrl(originalUrl);

        assertEquals(existingUrl, result);

        // Verify that the repository methods are called
        verify(urlRepository, times(1)).findByOriginalUrl(originalUrl);
        verify(urlRepository, never()).findByTinyUrl(anyString());
        verify(urlRepository, times(0)).save(any(Url.class));
    }

    @Test
    public void testShortenUrlWhenUrlDoesNotExist() {
        String originalUrl = "http://example.com";
        String generatedCode = "abc123";
        Url newUrl = Url.builder().tinyUrl(generatedCode).originalUrl(originalUrl).build();

        when(urlRepository.findByOriginalUrl(originalUrl)).thenReturn(null);
        when(codeGenerator.generateUniqueCode(originalUrl)).thenReturn(generatedCode);
        when(urlRepository.findByTinyUrl(generatedCode)).thenReturn(null);
        when(urlRepository.save(any(Url.class))).thenReturn(newUrl);

        Url result = tinyUrl.shortenUrl(originalUrl);

        assertEquals(newUrl, result);

        // Verify that the repository methods are called
        verify(urlRepository, times(1)).findByOriginalUrl(originalUrl);
        verify(urlRepository, times(1)).findByTinyUrl(generatedCode);
        verify(urlRepository, times(1)).save(any(Url.class));
    }
}
