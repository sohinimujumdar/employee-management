package com.example.employee.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

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
//        model.addAttribute("roles", authenticationToken.getAuthorities());

        return "post-login";
    }

}