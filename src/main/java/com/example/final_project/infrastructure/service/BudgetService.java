package com.example.final_project.infrastructure.service;

import com.example.final_project.api.responses.StatusBudgetResponse;
import com.example.final_project.domain.budgets.Budget;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.error.ApplicationProcessError;
import com.example.final_project.infrastructure.command.budget.CreateBudgetCommand;
import com.example.final_project.infrastructure.command.budget.UpdateBudgetCommand;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BudgetService {


    Either<ApplicationProcessError, Budget> getBudgetById(BudgetId budgetId, String userId);

    Either<ApplicationProcessError, Optional<Budget>> deleteBudgetById(BudgetId budgetId, String userId);

    Page<Budget> findAllByPage(String userId, Pageable pageable);

    Budget registerNewBudget(CreateBudgetCommand command, String userId);

    Either<ApplicationProcessError, Budget> updateBudgetById(UpdateBudgetCommand command, String userId);

    StatusBudgetResponse getBudgetStatus(BudgetId budgetId, String userId);


}
