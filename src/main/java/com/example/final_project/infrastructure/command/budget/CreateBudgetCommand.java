package com.example.final_project.infrastructure.command.budget;

import com.example.final_project.domain.budgets.TypeOfBudget;

import java.math.BigDecimal;


public record CreateBudgetCommand(String title,
                                  BigDecimal limit,
                                  TypeOfBudget typeOfBudget,
                                  BigDecimal maxSingleExpense

) {

}
