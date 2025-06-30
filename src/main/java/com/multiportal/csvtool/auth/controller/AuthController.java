package com.multiportal.csvtool.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/.well-known/appspecific/com.chrome.devtools.json")
    public String ignoreDevtoolsRequest() {
        // just in case the error of devtools shows, maybe this solves that
        return "redirect:/";
    }
}
