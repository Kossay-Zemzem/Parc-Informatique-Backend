package com.project.parc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AngularRedirectController {
    // Redirect all routes (except those containing a period) to index.html for Angular routing
    @GetMapping("/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }

}