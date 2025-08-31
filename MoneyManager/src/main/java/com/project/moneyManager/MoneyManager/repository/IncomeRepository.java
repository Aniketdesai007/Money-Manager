package com.project.moneyManager.MoneyManager.repository;

import com.project.moneyManager.MoneyManager.entity.ExpenseEntity;
import com.project.moneyManager.MoneyManager.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository extends JpaRepository<IncomeEntity,Long> {
    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);
    List<IncomeEntity> findTop5ByProfileIdOrderByDateDesc(Long profileId);

    @Query(
            "SELECT SUM(e.amount) FROM IncomeEntity e WHERE e.profile.id=:profileId"
    )
    List<IncomeEntity> findTotalIncomeByProfileId(@Param("profileId") Long profileId);

    List<IncomeEntity> findByProfileIDAndDateBetweenAndNameContainingIgnoreCase(Long profileId,
                                                                                 LocalDate startDate,
                                                                                 LocalDate endDate,
                                                                                 String keyword,
                                                                                 Sort sort);

    List<IncomeEntity> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);



}
