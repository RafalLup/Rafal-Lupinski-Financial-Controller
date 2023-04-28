package com.example.final_project.config;

import com.example.final_project.domain.budgets.TypeOfBudget;

import java.math.BigDecimal;

public interface TestDataProvider {
    String PASSWORD = "rafalek12345";
    String USERNAME = "rafalek";
    String TITLE_BUDGET_OR_EXPENSE = "title";
    String UPDATE_TITLE_FOR_BUDGET_OR_EXPENSE= "update title";
    TypeOfBudget TYPE_OF_BUDGET = TypeOfBudget.HALF;
    BigDecimal BUDGET_LIMIT = BigDecimal.valueOf(33);
    BigDecimal EXPANSE_AMOUNT = BigDecimal.valueOf(33);
    BigDecimal UPDATE_BUDGET_LIMIT=BigDecimal.valueOf(34);
    BigDecimal UPDATE_EXPANSE_AMOUNT=BigDecimal.valueOf(10);
    BigDecimal BUDGET_MAX_SIMPLE_EXPENSE = BigDecimal.valueOf((333));
    BigDecimal UPDATE_MAX_SIMPLE_EXPENSE = BigDecimal.valueOf(334);

}
