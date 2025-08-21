package com.project.moneyManager.MoneyManager.controller;

import com.project.moneyManager.MoneyManager.dto.ProfileDTO;
import com.project.moneyManager.MoneyManager.service.ProfileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProfileController {
    public  final ProfileService service;
    @PostMapping("/register")
    public ResponseEntity<ProfileDTO>register(@RequestBody ProfileDTO profileDTO){
       ProfileDTO profileDTO1 =service.registerProfile(profileDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(profileDTO1);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> active(@RequestParam String token){
       Boolean isactive= service.activateToken(token);
        if (isactive){
            return ResponseEntity.ok("Profile Activated Successfully...");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation Token not  found or is already used..");
        }

    }

}
