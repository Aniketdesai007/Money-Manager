package com.project.moneyManager.MoneyManager.controller;

import com.project.moneyManager.MoneyManager.dto.AuthDTO;
import com.project.moneyManager.MoneyManager.dto.ProfileDTO;
import com.project.moneyManager.MoneyManager.service.ProfileService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/login")
    public ResponseEntity<Map<String,Object>> login(@RequestBody AuthDTO authDTO){
      try{
          if (!service.isAccountActive(authDTO.getEmail())){
              return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Account is not Active Please activate Account first!"));

          }
          else {
              Map<String,Object>response=service.authenticateAndGenerateToken(authDTO);
              return ResponseEntity.ok(response);
          }
      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      }
    }



}
