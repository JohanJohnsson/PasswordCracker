package org.example.passwordcracker.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    private String username;
    private String password;
    private String firstName;
    private boolean enabled =  false;
    private String resetToken;
    private LocalDateTime resetTokenCreationTime;
    private String emailVerificationToken;
    private LocalDateTime emailTokenExpiration;

}