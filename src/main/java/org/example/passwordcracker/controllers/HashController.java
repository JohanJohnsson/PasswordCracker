package org.example.passwordcracker.controllers;

import org.example.passwordcracker.dto.HashResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class HashController {
        
    @GetMapping("/search")
    public String searchPage() {
        return "search";
    }

    @PostMapping("/generate-hash")
    public String generateHash(@RequestParam("inputText") String inputText, Model model) {
        String md5Hash = calculateMD5(inputText);
        String sha256Hash = calculateSHA256(inputText);

        model.addAttribute("hashResult", new HashResult(md5Hash, sha256Hash));

        return "index";
    }

    @PostMapping("/search-hash")
    public String searchHash(@RequestParam("inputHash") String inputHash, Model model) {
        String password = findPasswordByHash(inputHash);
        if (password != null) {
            model.addAttribute("passwordResult", password);
        } else {
            model.addAttribute("passwordResult", "Ingen matchande lösenord hittades.");
        }
        return "search";
    }

    private String findPasswordByHash(String inputHash) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/hashes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String text = parts[0];
                    String algorithm = parts[1];
                    String hash = parts[2];

                    if (hash.equals(inputHash)) {
                        return text;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String calculateMD5(String input) {
        return hashString(input, "MD5");
    }

    private String calculateSHA256(String input) {
        return hashString(input, "SHA-256");
    }

    private String hashString(String input, String algorithm) {
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
