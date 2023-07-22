package com.tiny.url.services;

import com.tiny.url.models.Url;
import com.tiny.url.repository.UrlRepository;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
public class TinyUrl {

    UrlRepository urlRepository;

    private static final int CODE_LENGTH = 8; // Length of the shortened code
    private static final String BASE62_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final MessageDigest md;

    public TinyUrl(UrlRepository urlRepository) throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance("SHA-256");
        this.urlRepository = urlRepository;
    }

    public Url shortenUrl(String originalUrl) {
        Url findOld = urlRepository.findByOriginalUrl(originalUrl);
        if (findOld != null) {
            return findOld;
        }
        String code;
        Url findPart2;
        do {
            code = generateUniqueCode(originalUrl);
            findPart2 = urlRepository.findByTinyUrl(code);
        } while (findPart2 != null);
        Url newEntry = new Url(code, originalUrl);
        urlRepository.save(newEntry);
        return newEntry;
    }

    private String generateUniqueCode(String originalUrl) {
        byte[] messageDigest = md.digest(originalUrl.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hexHash = no.toString(16);

        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = Integer.parseInt(hexHash.substring(i * 4, (i + 1) * 4), 16) % BASE62_CHARACTERS.length();
            codeBuilder.append(BASE62_CHARACTERS.charAt(index));
        }

        return codeBuilder.toString();
    }
}
