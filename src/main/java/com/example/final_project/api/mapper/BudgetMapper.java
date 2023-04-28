package com.example.final_project.api.mapper;


import com.example.final_project.api.requests.RegisterBudgetRequest;
import com.example.final_project.api.responses.BudgetResponse;
import com.example.final_project.api.responses.StatusBudgetResponse;
import com.example.final_project.domain.budgets.Budget;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.infrastructure.command.budget.CreateBudgetCommand;
import com.example.final_project.infrastructure.command.budget.UpdateBudgetCommand;

import javax.validation.Valid;
import java.math.BigDecimal;


public class BudgetMapper {
    public static CreateBudgetCommand mapFromRequest(@Valid final RegisterBudgetRequest request) {
        return new CreateBudgetCommand(
                request.title(),
                request.limit(),
                request.typeOfBudget(),
                request.maxSingleExpense()
        );

    }

    public static UpdateBudgetCommand mapFromRequest(final String budgetId, final RegisterBudgetRequest request) {
        return new UpdateBudgetCommand(
                new BudgetId(budgetId),
                request.title(),
                request.limit(),
                request.typeOfBudget(),
                request.maxSingleExpense()
        );
    }

    public static BudgetResponse mapToResponse(final Budget budget) {
        return new BudgetResponse(
                budget.budgetId().value(),
                budget.title(),
                budget.limit(),
                budget.typeOfBudget(),
                budget.maxSingleExpense()
        );
    }

    public static StatusBudgetResponse toResponse(final String budgetId,
                                                  final Integer totalExpNumber,
                                                  final BigDecimal totalExpenseValue,
                                                  final BigDecimal amountLeft,
                                                  final BigDecimal budgetFullFillPerc,
                                                  final String title,
                                                  final String limitValue) {
        return new StatusBudgetResponse(
                budgetId,
                totalExpNumber,
                totalExpenseValue,
                amountLeft,
                budgetFullFillPerc,
                title,
                limitValue
        );
    }


}
