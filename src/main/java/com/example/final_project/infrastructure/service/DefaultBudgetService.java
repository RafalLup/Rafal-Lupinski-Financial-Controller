package com.example.final_project.infrastructure.service;

import com.example.final_project.api.mapper.BudgetMapper;
import com.example.final_project.api.responses.StatusBudgetResponse;
import com.example.final_project.domain.budgets.Budget;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.error.ApplicationProcessError;
import com.example.final_project.domain.error.BudgetError;
import com.example.final_project.domain.expenses.Expense;
import com.example.final_project.infrastructure.command.budget.CreateBudgetCommand;
import com.example.final_project.infrastructure.command.budget.UpdateBudgetCommand;
import com.example.final_project.infrastructure.repository.BudgetRepository;
import com.example.final_project.infrastructure.repository.ExpenseRepository;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class DefaultBudgetService implements BudgetService {

    private final BudgetRepository budgetRepository;

    private final ExpenseRepository expenseRepository;

    private final Supplier<BudgetId> budgetIdSupplier;

    public DefaultBudgetService(final BudgetRepository budgetRepository, final ExpenseRepository expenseRepository, final Supplier<BudgetId> budgetIdSupplier) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.budgetIdSupplier = budgetIdSupplier;
    }

    @Override
    public Budget registerNewBudget(final CreateBudgetCommand command, final String userId) {
        final Budget budget = new Budget(budgetIdSupplier.get(),
                command.title(),
                command.limit(),
                command.typeOfBudget(),
                command.maxSingleExpense(),
                userId);
        final Budget savedBudget = budgetRepository.save(budget);
        return savedBudget;
    }


    @Override
    public Either<ApplicationProcessError, Budget> getBudgetById(final BudgetId budgetId, final String userId) {
        final Optional<Budget> budget = budgetRepository.findBudgetByBudgetIdAndUserId(budgetId, userId);
        return budget.<Either<ApplicationProcessError, Budget>>map(Either::right).orElseGet(() -> Either.left(BudgetError.notFound(budgetId)));
    }

    @Override
    public Either<ApplicationProcessError, Optional<Budget>> deleteBudgetById(final BudgetId budgetId, final String userId) {
        final Optional<Budget> budgetToDelete = budgetRepository.deleteBudgetByBudgetIdAndUserId(new BudgetId(budgetId.value()), userId);
        if (budgetToDelete.isPresent()) {
            return Either.right(budgetToDelete);
        }
        return Either.left(BudgetError.notFound(budgetId));
    }


    @Override
    public Either<ApplicationProcessError, Budget> updateBudgetById(final UpdateBudgetCommand command, final String userId) {
        final Optional<Budget> budgetToUpdate = budgetRepository.findBudgetByBudgetIdAndUserId(command.budgetId(), userId);
        if (budgetToUpdate.isEmpty()) {
            return Either.left(BudgetError.notFound(command.budgetId()));
        }
        Budget updateBudget = new Budget(
                budgetToUpdate.get().budgetId(),
                command.title(),
                command.limit(),
                command.typeOfBudget(),
                command.maxSingleExpense(),
                userId
        );
        return Either.right(budgetRepository.save(updateBudget));
    }

    @Override
    public Page<Budget> findAllByPage(final String userId, final Pageable pageable) {
        return budgetRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public StatusBudgetResponse getBudgetStatus(final BudgetId budgetId, final String userId) {
        final Optional<Budget> budget = budgetRepository.findBudgetByBudgetIdAndUserId(budgetId, userId);

        final BigDecimal amountLeft = budget.get().limit().subtract(totalExpenseValue(budgetId, userId));

        final BigDecimal budgetFullFillPerc = budgetFullFillPerc(
                budget.get().limit(),
                totalExpenseValue(budgetId, userId));

        final Integer totalExpNumber = expenseRepository.findExpenseByBudgetIdAndUserId(budgetId,
                userId).size();

        final String limitValue = getLimitFromBudget(budget.get());

        final StatusBudgetResponse statusBudgetResponse = BudgetMapper.toResponse(
                budgetId.value(),
                totalExpNumber,
                totalExpenseValue(budgetId, userId),
                amountLeft,
                budgetFullFillPerc,
                budget.get().typeOfBudget().getTitle(),
                limitValue);
        return statusBudgetResponse;
    }


    private BigDecimal totalExpenseValue(final BudgetId budgetId, final String userId) {
        final BigDecimal totalExpenseAmount = expenseRepository.findExpenseByBudgetIdAndUserId(budgetId, userId)
                .stream()
                .map(Expense::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalExpenseAmount;
    }

    private BigDecimal budgetFullFillPerc(final BigDecimal base, final BigDecimal actual) {
        BigDecimal result = BigDecimal.ZERO;
        if (base.compareTo(BigDecimal.ZERO) != 0) {
            result = actual.divide(base, 2, RoundingMode.UP).multiply(BigDecimal.valueOf(100));
        }
        return result;
    }

    private String getLimitFromBudget(final Budget budget) {
        final BigDecimal limit = budget.limit().multiply(budget.typeOfBudget().getValue());
        if (!budget.typeOfBudget().getValue().equals(BigDecimal.valueOf(-1))) {

            return limit.toString();
        }
        return "no Limit";
    }


}
