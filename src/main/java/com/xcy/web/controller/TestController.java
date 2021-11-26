package com.xcy.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xuchunyang
 * @date 2021-11-26
 */
@RestController
public class TestController {

    @GetMapping("/ruok")
    public String ruok() {
        return "I am OK";
    }
}
