package com.topawar.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康度测试
 * @author topawar
 */
@RequestMapping("health")
@RestController
public class HealthController {

    @GetMapping("/")
    public String health(){
        return "ok";
    }
}
