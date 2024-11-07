package org.example.passwordcracker.controllers;

import org.example.passwordcracker.dto.HashResult;
import org.example.passwordcracker.service.HashService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HashController {
    
    private final HashService hashService;
    
    public HashController(HashService hashService) {
        this.hashService = hashService;
    }

    @GetMapping("/search")
    public String searchPage() {
        return "search";
    }

    @PostMapping("/generate-hash")
    public String generateHash(@RequestParam("inputText") String inputText, Model model) {
        String md5Hash = hashService.calculateMD5(inputText);
        String sha256Hash = hashService.calculateSHA256(inputText);

        model.addAttribute("hashResult", new HashResult(md5Hash, sha256Hash));

        return "index";
    }

    @PostMapping("/search-hash")
    public String searchHash(@RequestParam("inputHash") String inputHash, Model model) {
        String password = hashService.findPasswordByHashBinarySearch(inputHash);
        if (password != null) {
            model.addAttribute("passwordResult", password);
        } else {
            model.addAttribute("passwordResult", "Inget matchande lösenord hittades.");
        }
        return "search";
    }
    
}


