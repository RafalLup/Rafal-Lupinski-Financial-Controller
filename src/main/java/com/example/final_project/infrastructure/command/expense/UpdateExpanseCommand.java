package com.example.final_project.infrastructure.command.expense;

import com.example.final_project.domain.expenses.ExpenseId;

import java.math.BigDecimal;

public record UpdateExpanseCommand(ExpenseId expenseId,
                                   String title,
                                   BigDecimal amount) {
}
