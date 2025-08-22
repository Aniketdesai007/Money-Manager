package com.project.moneyManager.MoneyManager.service;

import com.project.moneyManager.MoneyManager.dto.AuthDTO;
import com.project.moneyManager.MoneyManager.dto.ProfileDTO;
import com.project.moneyManager.MoneyManager.entity.ProfileEntity;
import com.project.moneyManager.MoneyManager.repository.ProfileRepository;
import com.project.moneyManager.MoneyManager.util.Jwtutils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;
    private final PasswordEncoder passwordEncoder;
    private  final EmailService emailService;
    private  final AuthenticationManager authenticationManager;
    private  final Jwtutils jwtutils;


    public ProfileDTO registerProfile(ProfileDTO profileDTO){
        //helper funtion to convert profiledto to profile entity
       ProfileEntity newprofile= toProfileEntity(profileDTO);
        newprofile.setActivationToken(UUID.randomUUID().toString());
      newprofile= repository.save(newprofile);

      //send activation token
        String activationLink="http://localhost:8081/api/v1.0/activate?token="+newprofile.getActivationToken();

        String subject="Activate your Money Manager Account";
        String body="click on following link to activate account "+activationLink;

        emailService.sendMail(newprofile.getEmail(),subject,body);
       return  toDTO(newprofile);

       //helper funtion to convert entity to dto




    }

    public ProfileEntity toProfileEntity(ProfileDTO profileDTO){
    return     new ProfileEntity().builder()
            .id(profileDTO.getId())
            .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
                .createdAt(profileDTO.getCreatedAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .build();

    }


    public ProfileDTO toDTO(ProfileEntity profileEntity){
        return  new ProfileDTO().builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .profileImageUrl(profileEntity.getProfileImageUrl())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt())
                .build();
    }


    public  Boolean activateToken(String token){
     return    repository.findByActivationToken(token).map(profile->{
            profile.setIsActive(true);
            repository.save(profile);
            return true;
        }).orElse(false);
    }
    public Boolean isAccountActive(String email){
      return   repository.findByEmail(email)
                .map(data->data.getIsActive())
                .orElse(false);
    }


    public ProfileEntity getCurrentProfile(){

        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return repository.findByEmail( authentication.getName())
                .orElseThrow(()->new UsernameNotFoundException("Profile not found with email "+authentication.getName()));


    }


    public ProfileDTO getPublicProfile(String email){
        ProfileEntity currentProfile=null;
        if (email==null){
           currentProfile= getCurrentProfile();
        }else {
            currentProfile=repository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Profile not found with email "+email));
        }

        return  ProfileDTO.builder()
                .id(currentProfile.getId())
                .email(currentProfile.getEmail())
                .fullName(currentProfile.getFullName())
                .createdAt(currentProfile.getCreatedAt())
                .updatedAt(currentProfile.getUpdatedAt())
                .profileImageUrl(currentProfile.getProfileImageUrl())
                .build();


    }


    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
try{
    String token=jwtutils.generatetoken(authDTO.getEmail());
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(),authDTO.getPassword()));
    return Map.of(
            "token",token,
            "user",getPublicProfile(authDTO.getEmail())
    );

}catch (Exception e){
 throw  new RuntimeException("Invalid username or password!!");
}

    }
}
