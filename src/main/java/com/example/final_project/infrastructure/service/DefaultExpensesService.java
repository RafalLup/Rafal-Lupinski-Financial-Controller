package com.example.final_project.infrastructure.service;

import com.example.final_project.api.responses.StatusBudgetResponse;
import com.example.final_project.domain.budgets.Budget;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.error.ApplicationProcessError;
import com.example.final_project.domain.error.BudgetError;
import com.example.final_project.domain.error.ExpenseError;
import com.example.final_project.domain.expenses.Expense;
import com.example.final_project.domain.expenses.ExpenseId;
import com.example.final_project.infrastructure.command.expense.CreateExpenseCommand;
import com.example.final_project.infrastructure.command.expense.UpdateExpanseCommand;
import com.example.final_project.infrastructure.repository.BudgetRepository;
import com.example.final_project.infrastructure.repository.ExpenseRepository;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Service
public class DefaultExpensesService implements ExpensesService {

    private final ExpenseRepository expenseRepository;
    private final Supplier<ExpenseId> expenseIdSupplier;
    private final BudgetRepository budgetRepository;
    private final BudgetService budgetService;


    public DefaultExpensesService(ExpenseRepository expenseRepository, Supplier<ExpenseId> expenseIdSupplier, BudgetRepository budgetRepository, BudgetService budgetService) {
        this.expenseRepository = expenseRepository;
        this.expenseIdSupplier = expenseIdSupplier;
        this.budgetRepository = budgetRepository;

        this.budgetService = budgetService;
    }


    @Override
    public Either<ApplicationProcessError, Expense> getExpenseById(final ExpenseId expenseId, final String userId) {
        final Optional<Expense> findExpense = expenseRepository.findExpenseByExpenseIdAndUserId(expenseId, userId);
        return findExpense.<Either<ApplicationProcessError, Expense>>map(Either::right).orElseGet(() -> Either.left(ExpenseError.notFound(expenseId)));
    }


    @Override
    public Either<ApplicationProcessError, Optional<Expense>> deleteExpenseById(final ExpenseId expenseId, final String userId) {
        final Optional<Expense> expenseToDelete = expenseRepository.deleteExpenseByExpenseIdAndUserId(expenseId, userId);
        if (expenseToDelete.isPresent()) {
            return Either.right(expenseToDelete);
        }
        return Either.left(ExpenseError.notFound(expenseId));

    }


    @Override
    public Either<ApplicationProcessError, Expense> updateExpenseById(final UpdateExpanseCommand command,
                                                                      final String userId,
                                                                      final BudgetId budgetId) {
        final StatusBudgetResponse status = budgetService.getBudgetStatus(budgetId, userId);
        final Optional<Budget> budget = budgetRepository
                .findBudgetByBudgetIdAndUserId(budgetId, userId);
        if (budget.isEmpty()) {
            return Either.left(BudgetError.notFound(budgetId));
        }

        final Optional<Expense> expenseToUpdate = expenseRepository
                .findExpenseByExpenseIdAndUserId(command.expenseId(), userId);

        if (expenseToUpdate.isEmpty()) {
            return Either.left(ExpenseError.notFound(command.expenseId()));
        }


        if (command.amount().compareTo(budget.get().maxSingleExpense()) > 0) {
            return Either.left(ExpenseError
                    .expenseTooBig(budget.get().maxSingleExpense(), command.amount()));
        }

        if (command.amount().compareTo(status.amountLeft()) > 0) {
            return Either.left(BudgetError.amountLeft(command.amount(), status.amountLeft()));
        }

        Expense updateExpense = new Expense(
                command.expenseId(),
                command.title(),
                command.amount(), budgetId,
                userId);
        return Either.right(expenseRepository.save(updateExpense));

    }


    @Override
    public Page<Expense> findAllByPage(final String userId, final Pageable pageable) {
        return expenseRepository.findExpensesByUserId(userId, pageable);
    }

    @Override
    public Either<ApplicationProcessError, Expense> registerNewExpense(final CreateExpenseCommand command, final String budgetId, final String userId) {
        final Budget budget = budgetRepository.findBudgetByBudgetIdAndUserId(
                        new BudgetId(budgetId),
                        userId)
                .orElseThrow();

        StatusBudgetResponse budgetStatus = budgetService.getBudgetStatus(
                new BudgetId(budgetId),
                userId);

        final Expense expense = new Expense(
                expenseIdSupplier.get(),
                command.title(),
                command.amount(),
                new BudgetId(budgetId),
                userId);
        if (expense.amount().compareTo(budget.maxSingleExpense().multiply(budget.typeOfBudget().getValue())) > 0) {
            return Either.left(ExpenseError.expenseTooBig(
                    budget.maxSingleExpense().multiply(budget.typeOfBudget().getValue()),
                    command.amount()));
        }
        if (budgetStatus.amountLeft().compareTo(command.amount()) < 0) {
            return Either.left(ExpenseError.expenseExceedingTheBudget(budgetStatus.amountLeft(), command.amount()));
        }

        final Expense savedExpense = expenseRepository.save(expense);
        return Either.right(savedExpense);
    }
}
