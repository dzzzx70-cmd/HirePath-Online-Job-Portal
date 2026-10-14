package com.hirepath.controller;
import com.hirepath.model.User;
import com.hirepath.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Frontend aur Backend connection ke liye zaroori hai
public class UserController {

    @Autowired
    public UserRepository userRepository;

    // 1. Register API (Frontend se signup data save karega)
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        // Check karein ki email pehle se registered toh nahi hai
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            return "Error: Email is already registered!";
        }

        // Database me user save kar dein
        userRepository.save(user);
        return "User registered successfully!";
    }

    // 2. Login API (Frontend se login credentials verify karega)
    @PostMapping("/login")
    public String loginUser(@RequestBody User loginData) {
        User user = userRepository.findByEmail(loginData.getEmail());

        if (user != null && user.getPassword().equals(loginData.getPassword())) {
            // Yahan getRole() ka sahi use kiya gaya hai
            return "Login successful! Welcome " + user.getName() + " as " + user.getRole();
        }

        return "Invalid email or password!";
    }
}