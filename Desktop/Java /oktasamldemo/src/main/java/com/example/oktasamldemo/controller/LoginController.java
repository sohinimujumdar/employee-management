package com.example.oktasamldemo.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String redirectToSaml() {
        return "redirect:/saml2/authenticate/okta";
    }
}

