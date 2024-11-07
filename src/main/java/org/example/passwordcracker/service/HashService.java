package org.example.passwordcracker.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashService {

    public String findPasswordByHashBinarySearch(String inputHash) {
        String filePath = inputHash.length() == 32 ? "src/md5_hashes.txt" : "src/sha256_hashes.txt";

        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            long left = 0;
            long right = file.length() - 1;

            while (left <= right) {
                long mid = (left + right) / 2;

                file.seek(mid);

                if (mid != 0) {
                    while (mid > 0) {
                        file.seek(--mid);
                        if (file.read() == '\n') {
                            break;
                        }
                    }
                }

                String line = file.readLine();
                if (line == null) break;

                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String hash = parts[0];

                    int comparison = hash.compareTo(inputHash);
                    if (comparison == 0) {
                        return parts[2];
                    } else if (comparison < 0) {
                        left = file.getFilePointer();
                    } else {
                        right = mid - 1;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String calculateMD5(String input) {
        return hashString(input, "MD5");
    }

    public String calculateSHA256(String input) {
        return hashString(input, "SHA-256");
    }

    public String hashString(String input, String algorithm) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            byte[] hashBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error calculating hash", e);
        }
    }

}
