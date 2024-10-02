package com.tiny.url.helpers;

import com.tiny.url.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class CodeGenerator {

    private final MessageDigest messageDigest;

    @Autowired
    public CodeGenerator() throws NoSuchAlgorithmException {
        this.messageDigest = MessageDigest.getInstance("SHA-256");
    }

    public String generateUniqueCode(String originalUrl) {
        // Reset the digest to avoid carry-over from previous digests
        messageDigest.reset();

        // Generate the SHA-256 hash of the original URL
        byte[] digestBytes = messageDigest.digest(originalUrl.getBytes());

        // Convert the hash bytes to a hex string
        BigInteger bigInt = new BigInteger(1, digestBytes);
        StringBuilder hexHash = new StringBuilder(bigInt.toString(16));

        // Pad the hex string if necessary to ensure it meets the expected length
        while (hexHash.length() < 64) {
            hexHash.insert(0, "0");
        }

        // Generate a base62 code of the required length
        StringBuilder codeBuilder = new StringBuilder(Constants.CODE_LENGTH);
        for (int i = 0; i < Constants.CODE_LENGTH; i++) {
            int startIdx = i * 4;
            int endIdx = startIdx + 4;
            int index = Integer.parseInt(hexHash.substring(startIdx, endIdx), 16) % Constants.BASE62_CHARACTERS.length();
            codeBuilder.append(Constants.BASE62_CHARACTERS.charAt(index));
        }

        return codeBuilder.toString();
    }
}
