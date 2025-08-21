package com.project.moneyManager.MoneyManager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping({"/status","/health"})
@RestController
public class HealthCheckController {
    @GetMapping
    public  String HealthCheck(){
        return "Money Manager working fine!!";
    }
}
