package com.yx.nas.tool.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebDemoController {

    @GetMapping("/api/parent")
    public String sayHello(){
        return "parent";
    }
}
