package com.example.final_project.UnitTest;

import com.example.final_project.config.TestDataProvider;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.error.ApplicationProcessError;
import com.example.final_project.domain.error.ExpenseError;
import com.example.final_project.domain.expenses.Expense;
import com.example.final_project.domain.expenses.ExpenseId;
import com.example.final_project.infrastructure.command.expense.CreateExpenseCommand;
import com.example.final_project.infrastructure.command.expense.UpdateExpanseCommand;
import com.example.final_project.infrastructure.repository.BudgetRepository;
import com.example.final_project.infrastructure.repository.ExpenseRepository;
import com.example.final_project.infrastructure.service.BudgetService;
import com.example.final_project.infrastructure.service.DefaultExpensesService;
import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Supplier;

import static com.example.final_project.config.TestDataProvider.expenseId;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultExpensesServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private Supplier<ExpenseId> expenseIdSupplier;
    @Mock
    private BudgetRepository budgetRepository;
    @Mock
    private BudgetService budgetService;
    @InjectMocks
    private DefaultExpensesService expensesService;

    @Test
    void getExpenseById() {
        when(expenseRepository.findExpenseByExpenseIdAndUserId(
                TestDataProvider.expenseId,
                TestDataProvider.userId))
                .thenReturn(Optional.of(TestDataProvider.expense));

        final Either<ApplicationProcessError, Expense> expenseHappyPath = expensesService.getExpenseById(
                TestDataProvider.expenseId,
                TestDataProvider.userId);

        final Either<ApplicationProcessError, Expense> expenseUnHappyPath = expensesService.getExpenseById(
                TestDataProvider.expenseId2,
                TestDataProvider.userId);

        Assertions.assertTrue(expenseHappyPath.isRight());
        Assertions.assertEquals(TestDataProvider.expense, expenseHappyPath.get());
        Assertions.assertTrue(expenseUnHappyPath.isLeft());
        Assertions.assertTrue(expenseUnHappyPath.getLeft() instanceof ExpenseError);

        verify(expenseRepository).findExpenseByExpenseIdAndUserId(TestDataProvider.expenseId, TestDataProvider.userId);


    }

    @Test
    void deleteExpenseById() {
        when(expenseRepository.deleteExpenseByExpenseIdAndUserId(
                TestDataProvider.expenseId,
                TestDataProvider.userId))
                .thenReturn(Optional.of(TestDataProvider.expense));

        final Either<ApplicationProcessError, Optional<Expense>> expenseDeleteHappyPath = expensesService.deleteExpenseById(
                TestDataProvider.expenseId,
                TestDataProvider.userId);

        final Either<ApplicationProcessError, Expense> expenseUnHappyPath = expensesService.getExpenseById(
                TestDataProvider.expenseId2,
                TestDataProvider.userId);

        Assertions.assertTrue(expenseDeleteHappyPath.isRight());
        Assertions.assertTrue(expenseUnHappyPath.isLeft());
        Assertions.assertTrue(expenseUnHappyPath.getLeft() instanceof ExpenseError);

        verify(expenseRepository).deleteExpenseByExpenseIdAndUserId(TestDataProvider.expenseId, TestDataProvider.userId);
    }

    @Test
    void registerNewExpense() {

        final CreateExpenseCommand commandHappyPath = new CreateExpenseCommand(
                TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
                BigDecimal.valueOf(15));

        Mockito.when(budgetRepository.findBudgetByBudgetIdAndUserId(any(BudgetId.class), anyString()))
                .thenReturn(Optional.of(TestDataProvider.budget));

        Mockito.when(budgetService.getBudgetStatus(any(BudgetId.class), anyString()))
                .thenReturn(TestDataProvider.budgetStatus);

        Mockito.when(expenseRepository.save(any(Expense.class))).thenReturn(TestDataProvider.expense);


        final Either<ApplicationProcessError, Expense> resultHappyPath = expensesService.registerNewExpense(
                commandHappyPath,
                String.valueOf(TestDataProvider.budgetId),
                TestDataProvider.userId);
        final Expense savedExpense = resultHappyPath.get();

        Assertions.assertTrue(resultHappyPath.isRight());
        Assertions.assertEquals(TestDataProvider.expenseId, savedExpense.expenseId());
        Assertions.assertEquals(TestDataProvider.expense.title(), savedExpense.title());
        Assertions.assertEquals(TestDataProvider.expense.amount(), savedExpense.amount());
        Assertions.assertEquals(TestDataProvider.budgetId, savedExpense.budgetId());
        Assertions.assertEquals(TestDataProvider.userId, savedExpense.userId());

        Mockito.verify(budgetRepository).findBudgetByBudgetIdAndUserId(any(BudgetId.class), anyString());
        Mockito.verify(budgetService).getBudgetStatus(any(BudgetId.class), anyString());

    }

    @Test
    void updateExpenseById() {
        final Expense expenseToUpdate = new Expense(expenseId,
                TestDataProvider.UPDATE_TITLE_FOR_BUDGET_OR_EXPENSE,
                TestDataProvider.UPDATE_EXPANSE_AMOUNT,
                TestDataProvider.budgetId,
                TestDataProvider.userId);

        final UpdateExpanseCommand updateCommand = new UpdateExpanseCommand(expenseId,
                TestDataProvider.UPDATE_TITLE_FOR_BUDGET_OR_EXPENSE,
                TestDataProvider.UPDATE_EXPANSE_AMOUNT);


        when(budgetRepository.findBudgetByBudgetIdAndUserId(
                any(BudgetId.class),
                anyString()))
                .thenReturn(Optional.of(TestDataProvider.budget));

        when(expenseRepository.findExpenseByExpenseIdAndUserId(
                any(ExpenseId.class),
                anyString()))
                .thenReturn(Optional.of(TestDataProvider.expense));

        when(budgetService.getBudgetStatus(
                any(BudgetId.class),
                anyString()))
                .thenReturn(TestDataProvider.budgetStatus);

        when(expenseRepository.save(
                any(Expense.class)))
                .thenReturn(expenseToUpdate);

        final Either<ApplicationProcessError, Expense> result = expensesService.updateExpenseById(
                updateCommand,
                TestDataProvider.userId,
                TestDataProvider.budgetId);
        Expense updatedExpense = result.get();

        Assertions.assertTrue(result.isRight());
        Assertions.assertEquals(TestDataProvider.expenseId, updatedExpense.expenseId());
        Assertions.assertEquals(updateCommand.title(), updatedExpense.title());
        Assertions.assertEquals(updateCommand.amount(), updatedExpense.amount());
        Assertions.assertEquals(TestDataProvider.budgetId, updatedExpense.budgetId());
        Assertions.assertEquals(TestDataProvider.userId, updatedExpense.userId());

        Mockito.verify(budgetRepository).findBudgetByBudgetIdAndUserId(any(BudgetId.class), anyString());
        Mockito.verify(expenseRepository).findExpenseByExpenseIdAndUserId(any(ExpenseId.class), anyString());
        Mockito.verify(budgetService).getBudgetStatus(any(BudgetId.class), anyString());
        Mockito.verify(expenseRepository).save(any(Expense.class));
    }
}