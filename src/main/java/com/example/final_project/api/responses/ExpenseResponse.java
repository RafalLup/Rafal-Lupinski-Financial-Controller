package com.example.final_project.api.responses;

import java.math.BigDecimal;

public record ExpenseResponse(
        String title,
        String expenseId,
        BigDecimal amount
) {

}
