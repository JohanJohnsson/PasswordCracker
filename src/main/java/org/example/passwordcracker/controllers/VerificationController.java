package org.example.passwordcracker.controllers;

import org.example.passwordcracker.security.User;
import org.example.passwordcracker.security.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class VerificationController {

    private final UserRepo userRepository;

    public VerificationController(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token, Model model) {
        System.out.println("im here");
        User user = userRepository.findByEmailVerificationToken(token);
        System.out.println(user.getFirstName());
        if (user == null) {
            model.addAttribute("message", "Invalid verification token.");
            return "error";
        }

        if (user.getEmailTokenExpiration().isBefore(LocalDateTime.now())) {
            model.addAttribute("message", "Verification token has expired.");
            return "error";
        }

        user.setEnabled(true);
        user.setEmailVerificationToken(null);
        user.setEmailTokenExpiration(null);
        userRepository.save(user);

        model.addAttribute("message", "Your account has been verified! You can now log in.");
        return "login";
    }

}
