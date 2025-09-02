package com.project.moneyManager.MoneyManager.controller;

import com.project.moneyManager.MoneyManager.dto.ExpenseDto;
import com.project.moneyManager.MoneyManager.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {
private final ExpenseService expenseService;

@PostMapping
public ResponseEntity<ExpenseDto>addExpenses(@RequestBody ExpenseDto dto){
  ExpenseDto expenseDto=expenseService.addexpense(dto);
  return ResponseEntity.status(HttpStatus.CREATED).body(expenseDto);
}
@GetMapping
public ResponseEntity<List<ExpenseDto>>getallExpensesBetweendate(){
  return ResponseEntity.status(HttpStatus.OK).body(expenseService.getAllExpensesforCurrentProfile());
}
@DeleteMapping("/{id}")
public ResponseEntity<?>deleteExpense(@PathVariable Long id){
  expenseService.deleteExpenseById(id);
  return ResponseEntity.ok("Deleted successfully");
}
@GetMapping("/topFiveExpenses")
public ResponseEntity<List<ExpenseDto>> getTopFiveExpenses(){

  return ResponseEntity.status(HttpStatus.FOUND).body(expenseService.getFiveExpenses());
}

@GetMapping("/totalExpense")
public ResponseEntity<?>totalExpenses()
{
  return ResponseEntity.status(HttpStatus.OK).body("Total Expenses = "+expenseService.findTotalExpensesOfCurrentProfile());
}
}
