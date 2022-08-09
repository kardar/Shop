package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/")
    public String viewHomePage(){
        return "index";
    }

    @GetMapping("/login")
    public String viewLogin(){
        return "login";
    }
}
