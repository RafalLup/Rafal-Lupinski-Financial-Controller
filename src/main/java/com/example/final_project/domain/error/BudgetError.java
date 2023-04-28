
package com.example.final_project.domain.error;

import com.example.final_project.domain.budgets.BudgetId;
import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

@Builder
public class BudgetError implements ApplicationProcessError {

    private final String message;
    private final BudgetErrorCode errorCode;

    public static BudgetError notFound(final BudgetId id) {
        return BudgetError.builder()
                .errorCode(BudgetErrorCode.BUDGET_NOT_FOUND)
                .message(String.format("Budget with id = %s not found.", id))
                .build();
    }

    public static BudgetError amountLeft(final BigDecimal amount, final BigDecimal status) {
        return BudgetError.builder()
                .errorCode(BudgetErrorCode.BUDGET_AMOUNT_EXCEEDED)
                .message(String.format("Expense amount is %s, budget amount Left is %s", amount, status))
                .build();
    }

    public static BudgetError noLimit() {
        return BudgetError.builder()
                .errorCode(BudgetErrorCode.TO_BIG_LIMIT)
                .message(String.format("Budget the budget is not within the limit"))
                .build();
    }

    @Override
    public Integer getCode() {
        switch (errorCode) {
            case BUDGET_NOT_FOUND -> {
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
