package com.project.moneyManager.MoneyManager.service;

import com.project.moneyManager.MoneyManager.entity.ProfileEntity;
import com.project.moneyManager.MoneyManager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {

   private final ProfileRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ProfileEntity exsistingProfile=repository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Profile not found with email "+email));

        return User.builder()
                .username(exsistingProfile.getEmail())
                .password(exsistingProfile.getPassword())
                .authorities(Collections.emptyList())
                .build();
    }
}
