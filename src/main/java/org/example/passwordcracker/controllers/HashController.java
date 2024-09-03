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
import java.util.ArrayList;
import java.util.List;

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
            model.addAttribute("passwordResult", "Inget matchande lösenord hittades.");
        }
        return "search";
    }

    private String findPasswordByHash(String inputHash) {
        String filePath;
        boolean isMd5 = inputHash.length() == 32;
        
        filePath = isMd5 ? "src/md5_hashes.txt" : "src/sha256_hashes.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            List<String> lines = new ArrayList<>();
            String line;
            
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            
            int left = 0;
            int right = lines.size() - 1;

            while (left <= right) {
                int mid = (left + right) / 2;
                String[] parts = lines.get(mid).split(":");
                if (parts.length == 3) {
                    String hash = parts[0];
                    //Skriver ut för att kolla om binary search utförs korrekt
                    System.out.println("Kontrollerar hash: " + hash);
                    if (hash.equals(inputHash)) {
                        return parts[2];
                    } else if (hash.compareTo(inputHash) < 0) {
                        left = mid + 1;
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


