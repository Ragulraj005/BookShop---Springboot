package com.bookstorebe.bookstorebe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    @GetMapping("/req/login")
    public String login(){
        return "login"; // This returns login.html
    }
    @GetMapping("/index") // KEEP THIS ONE
    public String home(){
        return "index"; // This returns index.html
    }
    @GetMapping("/req/signup")
    public String signup() {
        return "signup"; 
    }
}