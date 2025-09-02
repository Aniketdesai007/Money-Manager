package com.project.moneyManager.MoneyManager.controller;

import com.project.moneyManager.MoneyManager.dto.ExpenseDto;
import com.project.moneyManager.MoneyManager.dto.IncomeDto;
import com.project.moneyManager.MoneyManager.service.IncomeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/income")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDto> addIncome(@RequestBody IncomeDto incomeDto){
       IncomeDto dto= incomeService.addincome(incomeDto);
       return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }



    @GetMapping
    public ResponseEntity<List<IncomeDto>>getallExpensesBetweendate(){
        return ResponseEntity.status(HttpStatus.OK).body(incomeService.getAllExpensesforCurrentProfile());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleteExpense(@PathVariable Long id){
        incomeService.deleteIncomeById(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
