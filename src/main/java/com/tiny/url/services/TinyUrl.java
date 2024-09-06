package com.tiny.url.services;

import com.tiny.url.helpers.CodeGenerator;
import com.tiny.url.models.Url;
import com.tiny.url.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TinyUrl {

    @Autowired
    private final UrlRepository urlRepository;

    @Autowired
    private final CodeGenerator codeGenerator;

    @Autowired
    public TinyUrl(UrlRepository urlRepository, CodeGenerator codeGenerator) {
        this.urlRepository = urlRepository;
        this.codeGenerator = codeGenerator;
    }

    public Url shortenUrl(String originalUrl) {
        Url findOld = urlRepository.findByOriginalUrl(originalUrl);
        if (findOld != null) {
            return findOld;
        }
        String code;
        Url findPart2;
        do {
            code = codeGenerator.generateUniqueCode(originalUrl);
            findPart2 = urlRepository.findByTinyUrl(code);
        } while (findPart2 != null);
        Url newEntry = Url.builder().tinyUrl(code).originalUrl(originalUrl).build();
        urlRepository.save(newEntry);
        return newEntry;
    }
}
