package com.project.moneyManager.MoneyManager.repository;

import com.project.moneyManager.MoneyManager.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    List<CategoryEntity> findByProfileEntity_Id(Long id);
   CategoryEntity findByIdAndProfileEntity_Id(Long id, Long profileId);
    List<CategoryEntity> findByTypeAndProfileEntity_Id(String type, Long profileId);
    Boolean existsByNameAndProfileEntity_Id(String name, Long id);
}
