package com.project.moneyManager.MoneyManager.controller;

import com.project.moneyManager.MoneyManager.dto.ExpenseDto;
import com.project.moneyManager.MoneyManager.dto.FilterDto;
import com.project.moneyManager.MoneyManager.dto.IncomeDto;
import com.project.moneyManager.MoneyManager.service.ExpenseService;
import com.project.moneyManager.MoneyManager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/filter")
@RequiredArgsConstructor
public class FilterController {
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

@PostMapping
public ResponseEntity<?>filterTransition(@RequestBody FilterDto filterDto){
    LocalDate startDate=filterDto.getStartDate()!=null?filterDto.getStartDate():LocalDate.MIN;
    LocalDate endDate=filterDto.getEndDate()!=null?filterDto.getEndDate():LocalDate.now();
    String keyword=filterDto.getKeyword()!=null?filterDto.getKeyword():"";
    Sort.Direction direction="desc".equalsIgnoreCase(filterDto.getSortOrder())?Sort.Direction.DESC:Sort.Direction.ASC;
    String sortField=filterDto.getSortField()!=null?filterDto.getSortField():"date";
    Sort sort=Sort.by(direction,sortField);
    if ("income".equalsIgnoreCase(filterDto.getType())){
        List<IncomeDto>filterIncomeDTO=incomeService.filterIncome(startDate,endDate,sort,keyword);
        return ResponseEntity.ok(filterIncomeDTO);
    } else if ("expense".equalsIgnoreCase(filterDto.getType())) {
        List<ExpenseDto>filterExpense=expenseService.filterExpense(startDate,endDate,sort,keyword);
        return ResponseEntity.ok(filterExpense);
    }else{
        return ResponseEntity.badRequest().body("Invalid Type. kindly provide 'income' or 'expense' type.");
    }

}


}
