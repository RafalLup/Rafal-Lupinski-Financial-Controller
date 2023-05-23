package com.example.final_project.config;

import com.example.final_project.api.responses.StatusBudgetResponse;
import com.example.final_project.domain.budgets.Budget;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.budgets.TypeOfBudget;
import com.example.final_project.domain.expenses.Expense;
import com.example.final_project.domain.expenses.ExpenseId;

import java.math.BigDecimal;

public interface TestDataProvider {
    String PASSWORD = "rafalek12345";
    String USERNAME = "rafalek";
    String TITLE_BUDGET_OR_EXPENSE = "title";
    String UPDATE_TITLE_FOR_BUDGET_OR_EXPENSE = "update title";
    TypeOfBudget TYPE_OF_BUDGET = TypeOfBudget.HALF;
    BigDecimal BUDGET_LIMIT = BigDecimal.valueOf(33);
    BigDecimal EXPANSE_AMOUNT = BigDecimal.valueOf(33);
    BigDecimal UPDATE_BUDGET_LIMIT = BigDecimal.valueOf(34);
    BigDecimal UPDATE_EXPANSE_AMOUNT = BigDecimal.valueOf(10);
    BigDecimal BUDGET_MAX_SIMPLE_EXPENSE = BigDecimal.valueOf((333));
    BigDecimal UPDATE_MAX_SIMPLE_EXPENSE = BigDecimal.valueOf(334);


    ExpenseId expenseId = new ExpenseId("expanse-id");
    ExpenseId expenseId2 = new ExpenseId("expanse-idd");
    BudgetId budgetId = new BudgetId("budget-id");
    String userId = "user-id";
    Expense expense = new Expense(expenseId,
            TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
            TestDataProvider.EXPANSE_AMOUNT,
            budgetId,
            userId);
    Budget budget = new Budget(budgetId,
            TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
            TestDataProvider.BUDGET_LIMIT,
            TestDataProvider.TYPE_OF_BUDGET,
            TestDataProvider.BUDGET_MAX_SIMPLE_EXPENSE,
            userId);
    StatusBudgetResponse budgetStatus = new StatusBudgetResponse(
            budgetId.toString(),
            1,
            BigDecimal.ZERO,
            BigDecimal.valueOf(31),
            BigDecimal.ZERO,
            TypeOfBudget.STRICT.toString(),
            TestDataProvider.UPDATE_MAX_SIMPLE_EXPENSE.toString()
    );

}
