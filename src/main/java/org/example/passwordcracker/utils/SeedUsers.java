package org.example.passwordcracker.utils;

import org.example.passwordcracker.security.UserDataSeeder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;


@ComponentScan
@Component
public class SeedUsers implements CommandLineRunner {

    @Autowired
    private UserDataSeeder userDataSeeder;

    @Override
    public void run(String... args) throws Exception {
        userDataSeeder.seedUsers();
    }
}