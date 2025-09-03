package com.project.moneyManager.MoneyManager.service;

import com.project.moneyManager.MoneyManager.dto.ExpenseDto;
import com.project.moneyManager.MoneyManager.dto.IncomeDto;
import com.project.moneyManager.MoneyManager.dto.RecentTransitionDTO;
import com.project.moneyManager.MoneyManager.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final ProfileService profileService;


    public Map<String,Object>getDashboardData(){
       ProfileEntity profileEntity= profileService.getCurrentProfile();

       Map<String ,Object>returnValue=new LinkedHashMap<>();
      List<IncomeDto>latestIncome= incomeService.getFiveIncomes();
      List<ExpenseDto>latestExpenses=expenseService.getFiveExpenses();
      List<RecentTransitionDTO>recentTransitions=Stream.concat(latestIncome.stream().map(income-> RecentTransitionDTO.builder()
                .id(income.getId())
                .name(income.getName())
                .date(income.getDate())
                .profileId(profileEntity.getId())
                .icon(income.getIcon())
                .updatedAt(income.getUpdatedAt())
                .createdAt(income.getCreatedAt())
                .amount(income.getAmount())
                .type("income").build()
        ),
                latestExpenses.stream().map(expense->RecentTransitionDTO.builder()
                        .id(expense.getId())
                        .name(expense.getName())
                        .date(expense.getDate())
                        .profileId(profileEntity.getId())
                        .icon(expense.getIcon())
                        .updatedAt(expense.getUpdatedAt())
                        .createdAt(expense.getCreatedAt())
                        .amount(expense.getAmount())
                        .type("expense").build()
                )).sorted((a,b)->{
                    int cmp=a.getDate().compareTo(b.getDate());
                    if(cmp==0 && a.getCreatedAt()!=null && b.getCreatedAt()!=null){
                        return  b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;


        }).toList();

      returnValue.put("totalBalance",incomeService.findTotalIncomeOfCurrentProfile().subtract(expenseService.findTotalExpensesOfCurrentProfile()));
      returnValue.put("totalIncome",incomeService.findTotalIncomeOfCurrentProfile());
      returnValue.put("totalExpenses",expenseService.findTotalExpensesOfCurrentProfile());
      returnValue.put("recent5Expenses",latestExpenses);
      returnValue.put("recent5Incomes",latestIncome);
      returnValue.put("recentTransition",recentTransitions);

      return  returnValue;



    }




}
