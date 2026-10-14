package com.hirepath;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String home() {
        return "Welcome to HirePath Online Job Portal Backend! Server is running successfully.";
    }
}