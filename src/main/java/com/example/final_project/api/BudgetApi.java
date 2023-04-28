package com.example.final_project.api;

public interface BudgetApi {

    String BUDGETS_BASE_PATH = "/budgets";
    String GET_BUDGET_BY_ID = "/{rawBudgetId}";

    String GET_BUDGET_BASE_PATH_PLUS_SLASH = "/budgets/";
    String GET_BUDGET_BY_ID_PLUS_SLASH_AND_STATUS = "/{rawBudgetId}/status";

}
