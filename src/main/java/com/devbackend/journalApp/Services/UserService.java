package com.devbackend.journalApp.Services;


import com.devbackend.journalApp.Entity.User;
import com.devbackend.journalApp.Repo.UserRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {


    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepo.findAll();
    }

    public User getById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public User addUser(User user) {
        try {
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password is required");
            }
            if (!isBcryptHash(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }
            return userRepo.save(user);
        }
        catch (Exception e){
            log.error("Error Occurred",e);
            throw new RuntimeException("Failed to add User ", e);
        }
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public User updateUser(User user) {
        User existing = userRepo.findByUsername(user.getUsername());
        if (existing == null) {
            return null;
        }
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existing.setUsername(user.getUsername());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            existing.setRole(user.getRole());
        }
        return userRepo.save(existing);
    }

    private boolean isBcryptHash(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
}
