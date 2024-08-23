package org.example.passwordcracker.controllers;

import org.springframework.ui.Model;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal OAuth2User oauthUser, @AuthenticationPrincipal UserDetails userDetails) {
        if (oauthUser != null) {
            model.addAttribute("name", oauthUser.getAttribute("name"));
        } else if (userDetails != null) {
            model.addAttribute("name", userDetails.getUsername());
        }
        return "index";
    }
}