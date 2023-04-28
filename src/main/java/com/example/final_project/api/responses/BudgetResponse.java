package com.example.final_project.api.responses;


import com.example.final_project.domain.budgets.TypeOfBudget;

import java.math.BigDecimal;

public record BudgetResponse(
        String budgetId,
        String title,
        BigDecimal limit,
        TypeOfBudget typeOfBudget,
        BigDecimal maxSingleExpense
) {

}
