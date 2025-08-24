package com.project.moneyManager.MoneyManager.controller;

import com.project.moneyManager.MoneyManager.dto.CategoryDto;
import com.project.moneyManager.MoneyManager.entity.CategoryEntity;
import com.project.moneyManager.MoneyManager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    public final CategoryService service;
    @PostMapping()
    public ResponseEntity<CategoryDto>CreateNewCategory(@RequestBody CategoryDto data){
        return ResponseEntity.ok(service.saveCategory(data));
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>>findallCurrentCategory(){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.findAllCurrentCategory());
    }
    @GetMapping("/type/{categoryType}")
    public ResponseEntity<?>allCategoryByTpe(@PathVariable String categoryType){
      List<CategoryEntity>allentity=service.getAllCategoryByType(categoryType);

      if (allentity==null ||allentity.isEmpty()){
       return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found with type "+categoryType);
      }else {
          List<CategoryDto>allDto=allentity.stream().map(entity -> {
              return service.toDTO(entity);
          }).collect(Collectors.toList());
          return ResponseEntity.ok(allDto);
      }
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<CategoryDto> updatedata(@RequestBody CategoryDto dto,@PathVariable long id){
        return ResponseEntity.ok(service.updateCategory(id,dto));
    }

}
