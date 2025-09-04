package com.project.moneyManager.MoneyManager.service;

import com.project.moneyManager.MoneyManager.dto.ExpenseDto;
import com.project.moneyManager.MoneyManager.entity.ProfileEntity;
import com.project.moneyManager.MoneyManager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;
    private final ExpenseService expenseService;
    private final EmailService emailService;
    @Value("${money.manager.frontend.url}")
    private String frontendUrl;

@Scheduled(cron = "0 0 22 * * *",zone = "IST")
public void senddailyIncomeExpenseRemainder(){
log.info("Job Started :sendDailyIncomeExpense()");
List<ProfileEntity>allProfile=profileRepository.findAll();
for (ProfileEntity profile:allProfile){

    String body="Hii "+profile.getFullName()+"<br><br>"
            +"This is friendly remainder to add income and expenses for today in money manager.<br><br>"
            +"<a href="+frontendUrl+"style='display:inline-block;padding:10px 20px; background-color: #4CAF50;color:#fff;text-decoration:none; border-radius:5px;font-weight:bold;'>Go to Money Manager</a>"
            + "<br><br>Best regards, <br>Money Manager Team";

    emailService.sendMail(profile.getEmail(),"Daily Remainder : to add income and expenses to money Manager",body);
}
    log.info("Job Completed :sendDailyIncomeExpense()");


    }


//@Scheduled(cron = "0 0 23 * * *",zone = "IST")
@Scheduled(cron = "0 0 23 * * *",zone = "IST")
public void sendDailyExpenses(){

    log.info("Job Started : SendingdailyExpenses()");
  List<ProfileEntity>profiles=profileRepository.findAll();
for (ProfileEntity profile:profiles){

   List<ExpenseDto>todaysExpenses=expenseService.getExpensesOfUserOnDate(profile.getId(), LocalDate.now());

   if (!todaysExpenses.isEmpty())
   {
       StringBuilder table=new StringBuilder();
       table.append("<table style='border-collapse:collapse; width: 100%; '>");
       table.append("<tr style='background-color:#f2f2f2;'><th style='border:1px solid #ddd;padding: 8px;' >S.No</th><th style='border:1px solid #ddd; padding: 8px; '>Name</th><th style='border:1px solid #ddd;padding: 8px;'>Amount </th><th style='border:1px solid #ddd;padding: 8px; '>Category</th></tr>");
        int i=1;
        for (ExpenseDto expenseDto:todaysExpenses){
            table.append("<tr>");
            table.append("<td style='border:1px solid #ddd; padding: 8px;'>").append(i++).append("</td>");
            table.append("<td style='border:1px solid #ddd; padding: 8px;'>").append(expenseDto.getName()).append("</td>");
            table.append("<td style='border:1px solid #ddd; padding: 8px;'>").append(expenseDto.getAmount()).append("</td>");
            table.append("<td style='border:1px solid #ddd; padding: 8px;'>").append(expenseDto.getCategoryId()!=null?expenseDto.getCategoryName():"N/A").append("</td>");
            table.append("</tr>");

        }
       table.append("</table>");
String body="hii "+profile.getFullName()+",</br></br> here is you today's Expense summary <br/><br/>"+table+"<br/><br/> Best Regards<br/>Money Manager Team";
       emailService.sendMail(profile.getEmail(),"Your daily expense summary ",body);
   }




}
        log.info("Job Completed : SendingdailyExpenses()");



    }


}
