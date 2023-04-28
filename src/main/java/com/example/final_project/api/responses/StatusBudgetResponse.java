package com.example.final_project.api.responses;

import java.math.BigDecimal;

public record StatusBudgetResponse(String budgedId,
                                   Integer totalExpensesNumber,
                                   BigDecimal amountNow,
                                   BigDecimal amountLeft,
                                   BigDecimal budgetFullfillPerc,
                                   String typeOfBudget,
                                   String maxValue


) {

}
