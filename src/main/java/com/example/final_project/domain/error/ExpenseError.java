package com.example.final_project.domain.error;

import com.example.final_project.domain.expenses.ExpenseId;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static com.example.final_project.domain.error.ExpenseErrorCode.*;


@Builder
public class ExpenseError implements ApplicationProcessError {

    private final String message;
    private final ExpenseErrorCode errorCode;

    public static ExpenseError notFound(final ExpenseId expenseId) {
        return ExpenseError.builder()
                .errorCode(EXPENSE_NOT_FOUND)
                .message(String.format("Expense with id = %s not found.", expenseId))
                .build();
    }

    public static ExpenseError expenseTooBig(final BigDecimal maxSimpleExpense, final BigDecimal amount) {
        return ExpenseError.builder()
                .errorCode(OVER_BUDGET_LIMIT)
                .message(String.format("The budget limit which %s has been exceeded, it cannot add an expense of such value as %s", maxSimpleExpense, amount))
                .build();
    }

    public static ExpenseError expenseExceedingTheBudget(final BigDecimal amountLeft, final BigDecimal amount) {
        return ExpenseError.builder()
                .errorCode(EXPENSE_EXCEEDING_THE_BUDGET)
                .message(String.format("the remaining budget amount is %s and your spend is %s", amountLeft, amount))
                .build();
    }

    @Override
    public Integer getCode() {
        switch (errorCode) {
            case EXPENSE_NOT_FOUND -> {
                return HttpStatus.NOT_FOUND.value();
            }
            default -> {
                return HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
        }
    }


    @Override
    public String getReason() {
        return errorCode.name();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
