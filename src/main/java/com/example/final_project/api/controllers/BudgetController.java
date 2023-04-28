package com.example.final_project.api.controllers;

import com.example.final_project.api.BudgetApi;
import com.example.final_project.api.ExpanseApi;
import com.example.final_project.api.mapper.BudgetMapper;
import com.example.final_project.api.mapper.ErrorMapper;
import com.example.final_project.api.requests.RegisterBudgetRequest;
import com.example.final_project.api.responses.ApiErrorResponse;
import com.example.final_project.api.responses.BudgetResponse;
import com.example.final_project.api.responses.StatusBudgetResponse;
import com.example.final_project.domain.budgets.Budget;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.error.ApplicationProcessError;
import com.example.final_project.domain.users.BudgetAppUser;
import com.example.final_project.domain.users.UserContextProvider;
import com.example.final_project.infrastructure.command.budget.CreateBudgetCommand;
import com.example.final_project.infrastructure.command.budget.UpdateBudgetCommand;
import com.example.final_project.infrastructure.service.BudgetService;
import io.vavr.control.Either;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static com.example.final_project.api.BudgetApi.GET_BUDGET_BY_ID;


@RestController
@RequestMapping(BudgetApi.BUDGETS_BASE_PATH)
class BudgetController {

    private final BudgetService budgetService;


    BudgetController(final BudgetService budgetService) {
        this.budgetService = budgetService;

    }

    @GetMapping(BudgetApi.GET_BUDGET_BY_ID)
    ResponseEntity<?> getSingleBudget(
            @PathVariable final String rawBudgetId
    ) {
        final BudgetAppUser user = UserContextProvider.getUserContext();
        final Either<ApplicationProcessError, Budget> budgetById = budgetService.getBudgetById(
                new BudgetId(rawBudgetId)
                , user.userId().value());

        if (budgetById.isLeft()) {
            final ApplicationProcessError error = budgetById.getLeft();
            return ResponseEntity.status(error.getCode()).body(ErrorMapper.mapError(error));
        }

        return ResponseEntity.ok(BudgetMapper.mapToResponse(budgetById.get()));
    }

    @GetMapping(BudgetApi.GET_BUDGET_BY_ID_PLUS_SLASH_AND_STATUS)
    ResponseEntity<StatusBudgetResponse> getStatusBudget(
            @PathVariable final String rawBudgetId
    ) {
        final BudgetAppUser user = UserContextProvider.getUserContext();

        return ResponseEntity.ok().body(budgetService.getBudgetStatus(
                new BudgetId(rawBudgetId),
                user.userId().value()));
    }


    @GetMapping
    ResponseEntity<Page<BudgetResponse>> getBudgetByPage(
            final @RequestParam(required = false, defaultValue = "0") Integer page,
            final @RequestParam(required = false, defaultValue = "25") Integer size,
            final @RequestParam(required = false, defaultValue = "budgetId") String sortBy,
            final @RequestParam(required = false, defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        final BudgetAppUser user = UserContextProvider.getUserContext();

        return ResponseEntity.ok(budgetService.findAllByPage(user.userId().value(),
                        PageRequest.of(page, size, Sort.by(sortDirection, sortBy)))
                .map(BudgetMapper::mapToResponse));
    }

    @PostMapping
    ResponseEntity<BudgetResponse> registerNewBudget(
            @RequestBody @Valid final RegisterBudgetRequest request
    ) {
        final BudgetAppUser user = UserContextProvider.getUserContext();
        final CreateBudgetCommand command = BudgetMapper.mapFromRequest(request);
        final Budget newBudget = budgetService.registerNewBudget(
                command,
                user.userId().value());

        final BudgetResponse budgetResponseDto = BudgetMapper.mapToResponse(newBudget);


        return ResponseEntity.created(URI.create(ExpanseApi.GET_EXPENSES_BASE_PATH_PLUS_SLASH + budgetResponseDto.budgetId())).body(budgetResponseDto);
    }


    @PutMapping(BudgetApi.GET_BUDGET_BY_ID)
    ResponseEntity<?> updateBudget(@PathVariable final String rawBudgetId, @RequestBody @Valid final RegisterBudgetRequest request) {
        final BudgetAppUser user = UserContextProvider.getUserContext();
        final UpdateBudgetCommand command = BudgetMapper.mapFromRequest(rawBudgetId, request);

        final Either<ApplicationProcessError, Budget> budgetToUpdate = budgetService.updateBudgetById(
                command,
                user.userId().value());

        if (budgetToUpdate.isLeft()) {
            ApplicationProcessError error = budgetToUpdate.getLeft();
            return ResponseEntity.status(error.getCode()).body(ErrorMapper.mapError(error));
        }
        return ResponseEntity.ok(BudgetMapper.mapToResponse(budgetToUpdate.get()));
    }

    @DeleteMapping(GET_BUDGET_BY_ID)
    ResponseEntity<ApiErrorResponse> deleteBudgetById(@PathVariable final String rawBudgetId) {
        final BudgetAppUser user = UserContextProvider.getUserContext();

        final Either<ApplicationProcessError, Optional<Budget>> deleteResult = budgetService.deleteBudgetById(
                new BudgetId(rawBudgetId),
                user.userId().value());

        if (deleteResult.isLeft()) {
            final ApplicationProcessError errorResult = deleteResult.getLeft();
            return ResponseEntity.status(errorResult.getCode()).body(ErrorMapper.mapError(errorResult));
        }
        return ResponseEntity.ok().build();
    }

}
