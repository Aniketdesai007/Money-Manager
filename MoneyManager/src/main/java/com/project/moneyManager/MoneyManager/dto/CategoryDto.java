package com.project.moneyManager.MoneyManager.dto;

import com.project.moneyManager.MoneyManager.entity.ProfileEntity;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime UpdatedAt;
    private  String  type;
    private  String  icon;
    private Long profileId;
}
