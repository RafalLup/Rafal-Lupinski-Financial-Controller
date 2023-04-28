package com.example.final_project.infrastructure.repository;

import com.example.final_project.domain.budgets.Budget;
import com.example.final_project.domain.budgets.BudgetId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BudgetRepository extends MongoRepository<Budget, BudgetId> {

    Optional<Budget> findBudgetByBudgetIdAndUserId(BudgetId budgetId, String userId);

    Optional<Budget> deleteBudgetByBudgetIdAndUserId(BudgetId budgetId, String userId);

    Page<Budget> findAllByUserId(String userId, Pageable pageable);


}
