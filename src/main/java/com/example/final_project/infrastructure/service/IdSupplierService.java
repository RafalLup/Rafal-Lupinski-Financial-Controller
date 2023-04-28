package com.example.final_project.infrastructure.service;

import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.expenses.ExpenseId;
import com.example.final_project.domain.users.UserId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.Supplier;

@Configuration
public class IdSupplierService {
    @Bean
    public Supplier<BudgetId> budgetIdSupplier() {
        return () -> new BudgetId(UUID.randomUUID().toString());
    }

    @Bean
    public Supplier<ExpenseId> expenseIdSupplier() {
        return () -> new ExpenseId(UUID.randomUUID().toString());
    }

    @Bean
    public Supplier<UserId> userIdSupplier() {
        return () -> new UserId(UUID.randomUUID().toString());
    }
}
