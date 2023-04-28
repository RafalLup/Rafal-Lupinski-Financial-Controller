package com.example.final_project.api.controllers;

import com.example.final_project.api.ExpanseApi;
import com.example.final_project.api.mapper.ErrorMapper;
import com.example.final_project.api.mapper.ExpenseMapper;
import com.example.final_project.api.requests.RegisterExpenseRequest;
import com.example.final_project.api.responses.ApiErrorResponse;
import com.example.final_project.api.responses.ExpenseResponse;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.error.ApplicationProcessError;
import com.example.final_project.domain.expenses.Expense;
import com.example.final_project.domain.expenses.ExpenseId;
import com.example.final_project.domain.users.BudgetAppUser;
import com.example.final_project.domain.users.UserContextProvider;
import com.example.final_project.infrastructure.command.expense.CreateExpenseCommand;
import com.example.final_project.infrastructure.command.expense.UpdateExpanseCommand;
import com.example.final_project.infrastructure.service.ExpensesService;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(ExpanseApi.EXPENSES_BASE_PATH)
public class ExpensesController {
    private final ExpensesService expensesService;

    ExpensesController(final ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    @GetMapping(ExpanseApi.GET_EXPANSES_BY_ID)
    ResponseEntity<?> getSingleExpense(
            @PathVariable final String rawExpenseId
    ) {
        final BudgetAppUser user = UserContextProvider.getUserContext();
        final Either<ApplicationProcessError, Expense> expenseById = expensesService.getExpenseById(
                new ExpenseId(rawExpenseId),
                user.userId().value());

        if (expenseById.isLeft()) {
            final ApplicationProcessError error = expenseById.getLeft();
            return ResponseEntity.status(error.getCode()).body(ErrorMapper.mapError(error));
        }
        return ResponseEntity.ok(ExpenseMapper.mapToResponse(expenseById.get()));
    }

    @GetMapping
    ResponseEntity<Page<ExpenseResponse>> getExpensesByPage(
            final @RequestParam(required = false, defaultValue = "0") Integer page,
            final @RequestParam(required = false, defaultValue = "25") Integer size,
            final @RequestParam(required = false, defaultValue = "expenseId") String sortBy,
            final @RequestParam(required = false, defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        final BudgetAppUser user = UserContextProvider.getUserContext();
        return ResponseEntity.ok(expensesService.findAllByPage(user.userId().value(), PageRequest.of(page, size, Sort.by(sortDirection, sortBy)))
                .map(ExpenseMapper::mapToResponse));
    }


    @PostMapping
    ResponseEntity<?> registerNewExpense(
            @RequestBody @Valid final RegisterExpenseRequest request
    ) {
        final BudgetAppUser user = UserContextProvider.getUserContext();

        final CreateExpenseCommand command = ExpenseMapper.mapFromRequest(request);
        final Either<ApplicationProcessError, Expense> registerExpense = expensesService.registerNewExpense(
                command,
                request.budgetId(),
                user.userId().value());

        if (registerExpense.isLeft()) {
            final ApplicationProcessError error = registerExpense.getLeft();
            return ResponseEntity.status(error.getCode()).body(ErrorMapper.mapError(error));
        }
        final ExpenseResponse expenseResponse = ExpenseMapper.mapToResponse(registerExpense.get());

        return ResponseEntity.ok().body(expenseResponse);
    }

    @DeleteMapping(ExpanseApi.GET_EXPANSES_BY_ID)
    public ResponseEntity<ApiErrorResponse> deleteExpense(@PathVariable final String rawExpenseId) {
        final BudgetAppUser user = UserContextProvider.getUserContext();

        final Either<ApplicationProcessError, Optional<Expense>> expenseToDelete = expensesService.deleteExpenseById(
                new ExpenseId(rawExpenseId),
                user.userId().value());

        if (expenseToDelete.isLeft()) {
            final ApplicationProcessError errorResult = expenseToDelete.getLeft();
            return ResponseEntity.status(errorResult.getCode()).body(ErrorMapper.mapError(errorResult));
        }
        return ResponseEntity.ok().build();
    }


    @PutMapping(ExpanseApi.GET_EXPANSES_BY_ID)
    public ResponseEntity<?> updateExpense(@PathVariable final String rawExpenseId, @RequestBody @Valid final RegisterExpenseRequest request) {
        final BudgetAppUser user = UserContextProvider.getUserContext();
        final UpdateExpanseCommand command = ExpenseMapper.mapFromRequest(rawExpenseId, request);

        final Either<ApplicationProcessError, Expense> expensesToUpdate = expensesService.updateExpenseById(
                command,
                user.userId().value(),
                new BudgetId(request.budgetId()));

        if (expensesToUpdate.isLeft()) {
            final ApplicationProcessError error = expensesToUpdate.getLeft();
            return ResponseEntity.status(error.getCode()).body(ErrorMapper.mapError(error));
        }

        return ResponseEntity.ok(ExpenseMapper.mapToResponse(expensesToUpdate.get()));


    }

}
