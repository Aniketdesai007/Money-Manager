package com.project.moneyManager.MoneyManager.service;

import com.project.moneyManager.MoneyManager.dto.ProfileDTO;
import com.project.moneyManager.MoneyManager.entity.ProfileEntity;
import com.project.moneyManager.MoneyManager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    public final ProfileRepository repository;
    private  final EmailService emailService;

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
                .password(profileDTO.getPassword())
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


}
