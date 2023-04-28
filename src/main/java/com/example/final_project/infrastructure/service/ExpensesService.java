package com.example.final_project.infrastructure.service;

import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.error.ApplicationProcessError;
import com.example.final_project.domain.expenses.Expense;
import com.example.final_project.domain.expenses.ExpenseId;
import com.example.final_project.infrastructure.command.expense.CreateExpenseCommand;
import com.example.final_project.infrastructure.command.expense.UpdateExpanseCommand;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ExpensesService {

    Either<ApplicationProcessError, Expense> getExpenseById(ExpenseId expenseId, String userId);

    Either<ApplicationProcessError, Optional<Expense>> deleteExpenseById(ExpenseId expenseId, String userId);

    Either<ApplicationProcessError, Expense> updateExpenseById(UpdateExpanseCommand command, String userId, BudgetId budgetId);

    Page<Expense> findAllByPage(String userId, Pageable pageable);

    Either<ApplicationProcessError, Expense> registerNewExpense(CreateExpenseCommand command, String budgetId, String value);
}
