package com.example.final_project.infrastructure.service;

import com.example.final_project.config.TestDataProvider;
import com.example.final_project.domain.budgets.Budget;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.error.ApplicationProcessError;
import com.example.final_project.infrastructure.command.budget.CreateBudgetCommand;
import com.example.final_project.infrastructure.repository.BudgetRepository;
import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.function.Supplier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultBudgetServiceTest {
    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private Supplier<BudgetId> budgetIdSupplier;
    @InjectMocks
    private DefaultBudgetService defaultBudgetService;


    @Test
    void registerNewBudget() {
        final CreateBudgetCommand command = new CreateBudgetCommand(
                TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
                TestDataProvider.BUDGET_LIMIT,
                TestDataProvider.TYPE_OF_BUDGET,
                TestDataProvider.BUDGET_MAX_SIMPLE_EXPENSE
        );
        Mockito.when(budgetRepository.save(any(Budget.class))).thenReturn(TestDataProvider.budget);
        final Budget createBudget = defaultBudgetService.registerNewBudget(command, TestDataProvider.userId);

        Assertions.assertEquals(createBudget.userId(), TestDataProvider.budget.userId());
        Assertions.assertEquals(createBudget.title(), TestDataProvider.budget.title());
        Assertions.assertEquals(createBudget.budgetId(), TestDataProvider.budget.budgetId());
        Assertions.assertEquals(createBudget.typeOfBudget(), TestDataProvider.budget.typeOfBudget());
        Assertions.assertEquals(createBudget.maxSingleExpense(), TestDataProvider.budget.maxSingleExpense());

    }

    @Test
    void getBudgetById() {
        Mockito.when(budgetRepository.findBudgetByBudgetIdAndUserId(TestDataProvider.budgetId, TestDataProvider.userId)).thenReturn(Optional.of(TestDataProvider.budget));


        final Either<ApplicationProcessError, Budget> getBudgetById = defaultBudgetService.getBudgetById(TestDataProvider.budgetId, TestDataProvider.userId);
        final Budget checkBudget = getBudgetById.get();

        Assertions.assertTrue(getBudgetById.isRight());
        Assertions.assertEquals(checkBudget.budgetId(), TestDataProvider.budget.budgetId());
        Assertions.assertEquals(checkBudget.typeOfBudget(), TestDataProvider.budget.typeOfBudget());
        Assertions.assertEquals(checkBudget.maxSingleExpense(), TestDataProvider.budget.maxSingleExpense());
        Assertions.assertEquals(checkBudget.limit(), TestDataProvider.budget.limit());
        Assertions.assertEquals(checkBudget.title(), TestDataProvider.budget.title());
        Assertions.assertEquals(checkBudget.userId(), TestDataProvider.budget.userId());

        verify(budgetRepository).findBudgetByBudgetIdAndUserId(TestDataProvider.budgetId, TestDataProvider.userId);
    }

    @Test
    void deleteBudgetById() {
        Mockito.when(budgetRepository.deleteBudgetByBudgetIdAndUserId(
                TestDataProvider.budgetId,
                TestDataProvider.userId
        )).thenReturn(Optional.of(TestDataProvider.budget));

        final Either<ApplicationProcessError,Optional<Budget>> budgetToDelete = defaultBudgetService.deleteBudgetById(
                TestDataProvider.budgetId,
                TestDataProvider.userId
        );

        Assertions.assertTrue(budgetToDelete.isRight());

        verify(budgetRepository).deleteBudgetByBudgetIdAndUserId(
                TestDataProvider.budgetId,
                TestDataProvider.userId
        );





    }
}