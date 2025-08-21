package com.project.moneyManager.MoneyManager.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.sql.Update;

import java.time.LocalDateTime;

@Entity
@Table(name ="ProfileTable")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;
     private String fullName;
     @Column(unique = true)
    private String email;
    private String password;
    private String profileImageUrl;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String activationToken;

    @PrePersist
    public void prepersist(){
        if (this.isActive==null){
            this.isActive=false;
        }

    }



}
