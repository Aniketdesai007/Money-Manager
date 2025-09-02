package com.project.moneyManager.MoneyManager.service;
import com.project.moneyManager.MoneyManager.dto.ExpenseDto;
import com.project.moneyManager.MoneyManager.dto.IncomeDto;
import com.project.moneyManager.MoneyManager.entity.CategoryEntity;
import com.project.moneyManager.MoneyManager.entity.ExpenseEntity;
import com.project.moneyManager.MoneyManager.entity.IncomeEntity;
import com.project.moneyManager.MoneyManager.entity.ProfileEntity;
import com.project.moneyManager.MoneyManager.repository.CategoryRepository;
import com.project.moneyManager.MoneyManager.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {
    private final ProfileService profileService;
    private  final CategoryRepository categoryRepo;
    private final IncomeRepository incomeRepository;
    //helper method to convert expensedto to expense entity
    public IncomeEntity toIncomeentity(IncomeDto incomeDto, ProfileEntity profile, CategoryEntity category){
        return IncomeEntity.builder()
                .name(incomeDto.getName())
                .icon(incomeDto.getIcon())
                .date(incomeDto.getDate())
                .amount(incomeDto.getAmount())
                .category(category)
                .profile(profile)
                .build();
    }




    //helper method to convert expenseentity to expense dto
    public IncomeDto toIncomeDTO(IncomeEntity entity){
        return  IncomeDto.builder()
                .id(entity.getId())
                .amount(entity.getAmount())
                .date(entity.getDate())
                .categoryName(entity.getCategory()!=null?entity.getCategory().getName():null)
                .icon(entity.getIcon())
                .categoryId(entity.getCategory()!=null?entity.getCategory().getId():null)
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }



    public IncomeDto addincome(IncomeDto incomeDto){
        ProfileEntity profileEntity= profileService.getCurrentProfile();
        CategoryEntity categoryEntity= categoryRepo.findById(incomeDto.getCategoryId()).orElseThrow(()->new RuntimeException("Category not found with id "+incomeDto.getCategoryId()));

        IncomeEntity newIncomeEntity= toIncomeentity(incomeDto,profileEntity,categoryEntity);
        newIncomeEntity= incomeRepository.save(newIncomeEntity);

        return  toIncomeDTO(newIncomeEntity);

    }



    //retrive all expenses for current profile


    public List<IncomeDto> getAllExpensesforCurrentProfile(){

        ProfileEntity profileEntity= profileService.getCurrentProfile();

        LocalDate now=LocalDate.now();
        LocalDate startdate= now.withDayOfMonth(1);
        LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity>allExpenseEntity= incomeRepository.findByProfileIdAndDateBetween(profileEntity.getId(),startdate,endDate);

        List<IncomeDto>allDto= allExpenseEntity.stream().map(this::toIncomeDTO).toList();

        return allDto;

    }



    public void deleteIncomeById(Long expenseID){
        ProfileEntity entity= profileService.getCurrentProfile();
        IncomeEntity incomeEntity=  incomeRepository.findById(expenseID).orElseThrow(()->new RuntimeException("Expense not found with id "+entity.getId()));
        if (!incomeEntity.getProfile().equals(entity)){
            throw new RuntimeException("Unauthorized access to delete this content!!!");
        }else{
            incomeRepository.deleteById(incomeEntity.getId());

        }
    }



}
