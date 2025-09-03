package com.project.moneyManager.MoneyManager.controller;

import com.project.moneyManager.MoneyManager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashBoardController {
    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<Map<String,Object>>getDashboard(){
        return ResponseEntity.ok(dashboardService.getDashboardData());
    }


}
