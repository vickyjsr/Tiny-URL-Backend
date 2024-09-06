package com.tiny.url.controllers;

import com.tiny.url.models.Url;
import com.tiny.url.repository.UrlRepository;
import com.tiny.url.services.TinyUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin("/v1")
@RequestMapping("/v1")
public class UrlController {

    @Autowired
    UrlRepository urlRepository;

    @Autowired
    TinyUrl tinyUrl;

    @PostMapping("/newTinyUrl")
    public Url saveUrl(@RequestParam(value = "originalUrl") String originalUrl) {
        System.out.println(originalUrl);
        Url newUrl = tinyUrl.shortenUrl(originalUrl);
        System.out.println(newUrl.getTinyUrl());
        return newUrl;
    }

    @GetMapping("/getUrl")
    public Url getOriginalUrl(@RequestParam(value = "tinyUrl") String tinyUrl) {
        Url url = urlRepository.findByTinyUrl(tinyUrl);
        System.out.println(tinyUrl+"in getUrl api");
        System.out.println(url.toString());
        return url;
    }
}
