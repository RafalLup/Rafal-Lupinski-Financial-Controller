package com.example.final_project.infrastructure.command.expense;

import java.math.BigDecimal;

public record CreateExpenseCommand(String title,
                                   BigDecimal amount) {

}
