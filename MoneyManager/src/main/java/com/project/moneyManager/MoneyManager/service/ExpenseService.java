package com.project.moneyManager.MoneyManager.service;

import com.project.moneyManager.MoneyManager.dto.ExpenseDto;
import com.project.moneyManager.MoneyManager.entity.CategoryEntity;
import com.project.moneyManager.MoneyManager.entity.ExpenseEntity;
import com.project.moneyManager.MoneyManager.entity.ProfileEntity;
import com.project.moneyManager.MoneyManager.repository.CategoryRepository;
import com.project.moneyManager.MoneyManager.repository.ExpenseRpository;
import com.project.moneyManager.MoneyManager.repository.ProfileRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private  final CategoryRepository categoryRepo;
    private  final ProfileRepository profileRepository;

    private  final ProfileService profileService;

    private final ExpenseRpository expenseRpository;
    //helper method to convert expensedto to expense entity
    public ExpenseEntity toexpenseentity(ExpenseDto expenseDto, ProfileEntity profile, CategoryEntity category){
       return ExpenseEntity.builder()
                    .name(expenseDto.getName())
                    .icon(expenseDto.getIcon())
                    .date(expenseDto.getDate())
                    .amount(expenseDto.getAmount())
                    .category(category)
                    .profile(profile)
                    .build();
    }

    //helper method to convert expenseentity to expense dto


    public ExpenseDto toexpenseDTO(ExpenseEntity entity){
        return  ExpenseDto.builder()
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


    public ExpenseDto addexpense(ExpenseDto expenseDto){
          ProfileEntity profileEntity= profileService.getCurrentProfile();
         CategoryEntity categoryEntity= categoryRepo.findById(expenseDto.getCategoryId()).orElseThrow(()->new RuntimeException("Category not found with id "+expenseDto.getCategoryId()));

       ExpenseEntity newExpenseEntity= toexpenseentity(expenseDto,profileEntity,categoryEntity);
      newExpenseEntity= expenseRpository.save(newExpenseEntity);

      return  toexpenseDTO(newExpenseEntity);

    }



    //retrive all expenses for current profile


    public List<ExpenseDto> getAllExpensesforCurrentProfile(){

       ProfileEntity profileEntity= profileService.getCurrentProfile();

        LocalDate now=LocalDate.now();
       LocalDate startdate= now.withDayOfMonth(1);
       LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
       List<ExpenseEntity>allExpenseEntity= expenseRpository.findByProfileIdAndDateBetween(profileEntity.getId(),startdate,endDate);

      List<ExpenseDto>allDto= allExpenseEntity.stream().map(this::toexpenseDTO).toList();

      return allDto;

    }


    public void deleteExpenseById(Long expenseID){
       ProfileEntity entity= profileService.getCurrentProfile();
     ExpenseEntity expenseEntity=  expenseRpository.findById(expenseID).orElseThrow(()->new RuntimeException("Expense not found with id "+entity.getId()));
     if (!expenseEntity.getProfile().equals(entity)){
         throw new RuntimeException("Unauthorized access to delete this content!!!");
     }else{
         expenseRpository.deleteById(expenseEntity.getId());

     }
    }

}
