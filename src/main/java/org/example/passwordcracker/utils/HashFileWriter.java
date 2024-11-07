package org.example.passwordcracker.utils;

import org.example.passwordcracker.service.HashService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class HashFileWriter {
    
    private final HashService hashService;
    
    public HashFileWriter(HashService hashService) {
        this.hashService = hashService;
    }

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
                    String md5Hash = hashService.hashString(entry, "MD5");
                    String sha256Hash = hashService.hashString(entry, "SHA-256");

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
    
}


