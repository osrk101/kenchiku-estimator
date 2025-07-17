package com.kenchiku_estimator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordHashGenerator implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        String rawPassword = "password";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode(rawPassword);
        System.out.println("BCryptハッシュ: " + hashed);
    }
}
