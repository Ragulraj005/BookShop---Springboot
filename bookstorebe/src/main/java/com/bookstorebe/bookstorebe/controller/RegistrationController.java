package com.bookstorebe.bookstorebe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.bookstorebe.bookstorebe.entity.MyAppUser;
import com.bookstorebe.bookstorebe.repository.MyAppUserRepository;

import java.util.Optional; // Import Optional

@RestController
public class RegistrationController {
    @Autowired
    private MyAppUserRepository myAppUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public ResponseEntity<String> createUser(@RequestBody MyAppUser user) {
        Optional<MyAppUser> existingAppUser = myAppUserRepository.findByUsername(user.getUsername());

        if (existingAppUser.isPresent()) { // Use isPresent() for Optional
            return new ResponseEntity<>("User with this username already exists.", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        myAppUserRepository.save(user);

        return new ResponseEntity<>("Registration successful! You can now log in.", HttpStatus.OK);
    }
}
