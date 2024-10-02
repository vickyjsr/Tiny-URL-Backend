package com.tiny.url.services;

import com.tiny.url.constants.Constants;
import com.tiny.url.helpers.CodeGenerator;
import com.tiny.url.models.Url;
import com.tiny.url.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class TinyUrl {

    private final UrlRepository urlRepository;
    private final CodeGenerator codeGenerator;

    @Autowired
    public TinyUrl(UrlRepository urlRepository, CodeGenerator codeGenerator) {
        this.urlRepository = urlRepository;
        this.codeGenerator = codeGenerator;
    }

    @Transactional
    public Url shortenUrl(String originalUrl) {
        // Validate URL
        if (!isValidUrl(originalUrl)) {
            throw new IllegalArgumentException("Invalid URL provided");
        }

        // Check if the original URL already exists
        Url existingUrl = urlRepository.findByOriginalUrl(originalUrl);
        if (existingUrl != null) {
            return existingUrl;
        }

        // Generate unique code for the URL
        String code;
        Url urlWithSameCode;
        int retryCount = 0;
        final int MAX_RETRIES = Constants.MAX_RETRIES; // Avoid infinite loops

        do {
            code = codeGenerator.generateUniqueCode(originalUrl);
            urlWithSameCode = urlRepository.findByTinyUrl(code);
            retryCount++;
        } while (urlWithSameCode != null && retryCount < MAX_RETRIES);

        if (retryCount == MAX_RETRIES) {
            throw new IllegalStateException("Failed to generate unique tiny URL after several attempts, Please try Again");
        }

        // Create new entry for the URL
        Url newUrl = Url.builder()
                .tinyUrl(code)
                .originalUrl(originalUrl)
                .build();

        return urlRepository.save(newUrl); // Save and return the new URL
    }

    // Helper method to validate URLs (could be extended to handle complex validations)
    private boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false; // Check for null or empty strings
        }

        // Regex pattern for validating URL with or without scheme (e.g., http, https, or just google.com)
        String urlPattern = "^(https?://)?"              // Optional protocol (http or https)
                + "(([\\w\\d]([\\w\\d-]*[\\w\\d])*)\\.)+" // Domain name
                + "[a-zA-Z]{2,}"             // Top-level domain (e.g., .com, .org)
                + "(:\\d{1,5})?"             // Optional port
                + "(/.*)?$";                 // Optional path

        Pattern pattern = Pattern.compile(urlPattern);

        return pattern.matcher(url).matches();
    }

}
