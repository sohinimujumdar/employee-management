package com.example.employee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return """
                <html>
                <body>
                    <a href="/saml2/authenticate/okta">Login with SAML 2.0</a>
                </body>
                </html>
                """;
    }
}
