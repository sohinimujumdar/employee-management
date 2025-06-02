package com.example.employee.controller;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {


    @GetMapping("/login")
    public String loginPage() {
        return "login";  // your custom login page or spring security default
    }

    @GetMapping("/post-login")
    public String postLogin(@AuthenticationPrincipal OidcUser user,
                            Model model,
                            OAuth2AuthenticationToken authenticationToken) {

        // Get the ID token (JWT)
        String idToken = user.getIdToken().getTokenValue();

        model.addAttribute("idToken", idToken);    // Add JWT token here
        model.addAttribute("name", user.getFullName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("roles", authenticationToken.getAuthorities());

        return "post-login";
    }

    // Handle authentication exceptions like invalid login
    @ExceptionHandler(AuthenticationException.class)
    public String handleAuthenticationException(AuthenticationException ex, Model model) {
        model.addAttribute("error", "Authentication failed: " + ex.getMessage());
        return "login";  // Show login page with error message
    }
    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("error", "An error occurred: " + ex.getMessage());
        return "error";  // General error page
    }

}