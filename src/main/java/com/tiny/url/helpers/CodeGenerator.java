package com.tiny.url.helpers;

import com.tiny.url.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class CodeGenerator {

    private final MessageDigest md;

    public CodeGenerator() throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance("SHA-256");
    }

    public String generateUniqueCode(String originalUrl) {
        byte[] messageDigest = md.digest(originalUrl.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hexHash = no.toString(16);

        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < Constants.CODE_LENGTH; i++) {
            int index = Integer.parseInt(hexHash.substring(i * 4, (i + 1) * 4), 16) % Constants.BASE62_CHARACTERS.length();
            codeBuilder.append(Constants.BASE62_CHARACTERS.charAt(index));
        }

        return codeBuilder.toString();
    }
}
