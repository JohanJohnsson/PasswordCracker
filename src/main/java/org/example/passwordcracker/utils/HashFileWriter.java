package org.example.passwordcracker.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class HashFileWriter {

    @Bean
    public CommandLineRunner generateHashFiles() {
        return args -> {
            try (BufferedReader reader = new BufferedReader(new FileReader("src/passwords.txt"));
                 BufferedWriter md5Writer = new BufferedWriter(new FileWriter("src/md5_hashes.txt"));
                 BufferedWriter sha256Writer = new BufferedWriter(new FileWriter("src/sha256_hashes.txt"))) {

                List<String> md5Hashes = new ArrayList<>();
                List<String> sha256Hashes = new ArrayList<>();

                String entry;
                while ((entry = reader.readLine()) != null) {
                    String md5Hash = calculateHash(entry, "MD5");
                    String sha256Hash = calculateHash(entry, "SHA-256");
                    
                    md5Hashes.add(String.format("%s:MD5:%s", md5Hash, entry));
                    sha256Hashes.add(String.format("%s:SHA-256:%s", sha256Hash, entry));
                }
                
                Collections.sort(md5Hashes);
                Collections.sort(sha256Hashes);
                
                for (String hash : md5Hashes) {
                    md5Writer.write(hash);
                    md5Writer.newLine();
                }
                
                for (String hash : sha256Hashes) {
                    sha256Writer.write(hash);
                    sha256Writer.newLine();
                }

                System.out.println("MD5 and SHA-256 hashes written to separate files.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private String calculateHash(String input, String algorithm) {
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


