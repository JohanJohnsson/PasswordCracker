//package org.example.passwordcracker.utils;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.List;
//
//@Component
//public class HashFileWriter {
//
//    @Bean
//    public CommandLineRunner generateHashFile() {
//        return args -> {
//            List<String> dictionary = List.of(
//                    "123456",
//                    "password",
//                    "123456789",
//                    "12345678",
//                    "12345",
//                    "1234567",
//                    "1234567890",
//                    "qwerty",
//                    "abc123",
//                    "password1",
//                    "123123",
//                    "admin",
//                    "letmein",
//                    "welcome",
//                    "iloveyou",
//                    "sunshine",
//                    "monkey",
//                    "football",
//                    "charlie",
//                    "aa123456",
//                    "baseball",
//                    "superman",
//                    "qwertyuiop",
//                    "asdfghjkl",
//                    "1q2w3e4r",
//                    "zaq12wsx",
//                    "dragon",
//                    "michael",
//                    "jessica",
//                    "ashley",
//                    "sommar24",
//                    "vinter24",
//                    "test"
//            );
//
//            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/hashes.txt"))) {
//                for (String entry : dictionary) {
//                    String md5Hash = calculateHash(entry, "MD5");
//                    String sha256Hash = calculateHash(entry, "SHA-256");
//                    writer.write(String.format("%s:MD5:%s\n", entry, md5Hash));
//                    writer.write(String.format("%s:SHA-256:%s\n", entry, sha256Hash));
//                }
//                System.out.println("Hashes written to hashes.txt");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        };
//    }
//
//    private String calculateHash(String input, String algorithm) {
//        try {
//            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
//            byte[] hashBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
//            StringBuilder hashString = new StringBuilder();
//            for (byte b : hashBytes) {
//                hashString.append(String.format("%02x", b));
//            }
//            return hashString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error calculating hash", e);
//        }
//    }
//}

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

@Component
public class HashFileWriter {

    @Bean
    public CommandLineRunner generateHashFile() {
        return args -> {
            try (BufferedReader reader = new BufferedReader(new FileReader("src/passwords.txt"));
                 BufferedWriter writer = new BufferedWriter(new FileWriter("src/hashes.txt"))) {

                String entry;
                while ((entry = reader.readLine()) != null) {
                    String md5Hash = calculateHash(entry, "MD5");
                    String sha256Hash = calculateHash(entry, "SHA-256");
                    writer.write(String.format("%s:MD5:%s%n", entry, md5Hash));
                    writer.write(String.format("%s:SHA-256:%s%n", entry, sha256Hash));
                }

                System.out.println("Hashes written to hashes.txt");
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
