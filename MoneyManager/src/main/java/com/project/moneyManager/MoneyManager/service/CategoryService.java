package com.project.moneyManager.MoneyManager.service;

import com.project.moneyManager.MoneyManager.dto.CategoryDto;
import com.project.moneyManager.MoneyManager.entity.CategoryEntity;
import com.project.moneyManager.MoneyManager.entity.ProfileEntity;
import com.project.moneyManager.MoneyManager.repository.CategoryRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
private final CategoryRepository repository;
private final ProfileService profileService;

public  CategoryDto saveCategory(CategoryDto categoryDto){
   ProfileEntity profile= profileService.getCurrentProfile();
    if (repository.existsByNameAndProfileEntity_Id(categoryDto.getName(),profile.getId() )){
        throw new RuntimeException("category already exists with name"+categoryDto.getName());
    }
   CategoryEntity newCategory= toEntity(categoryDto,profile);
    newCategory=repository.save(newCategory);
return toDTO(newCategory);
}
//find all category of current profile
public List<CategoryDto>findAllCurrentCategory(){
    ProfileEntity currentProfile=profileService.getCurrentProfile();
    List<CategoryEntity>categories= repository.findByProfileEntity_Id(currentProfile.getId());
return categories.stream().map(singlecategory->{
   return toDTO(singlecategory);
}).collect(Collectors.toList());

}

//helpermethod
public CategoryEntity toEntity(CategoryDto categoryDto, ProfileEntity profileEntity){
    return  CategoryEntity.builder()
            .name(categoryDto.getName())
            .icon(categoryDto.getIcon())
            .profileEntity(profileEntity)
            .type(categoryDto.getType())
            .build();



}

public CategoryDto updateCategory(long categoryId,CategoryDto newDto){

  ProfileEntity entity=  profileService.getCurrentProfile();
 CategoryEntity exsistingData= repository.findByIdAndProfileEntity_Id(categoryId,entity.getId());

 if (exsistingData!=null){
     exsistingData.setType(newDto.getType());
    exsistingData.setName(newDto.getName());
    exsistingData.setIcon(newDto.getIcon());
    repository.save(exsistingData);
    return toDTO(exsistingData);
 }else {
     throw new RuntimeException("Data not found with id "+categoryId);

 }


}

public List<CategoryEntity>getAllCategoryByType(String type){
 ProfileEntity entity=  profileService.getCurrentProfile();
   List<CategoryEntity>allEntitiesByType= repository.findByTypeAndProfileEntity_Id(type, entity.getId());
  if (allEntitiesByType!=null){
      return allEntitiesByType;
  }
  return null;
}

public CategoryDto toDTO(CategoryEntity entity){
    return CategoryDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .profileId(entity.getProfileEntity()!=null?entity.getProfileEntity().getId():null)
            .createdAt(entity.getCreatedAt())
            .UpdatedAt(entity.getUpdatedAt())
            .type(entity.getType())
            .icon(entity.getIcon())
            .build();

}


}
