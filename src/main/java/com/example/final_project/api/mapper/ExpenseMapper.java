package com.example.final_project.api.mapper;


import com.example.final_project.api.requests.RegisterExpenseRequest;
import com.example.final_project.api.responses.ExpenseResponse;
import com.example.final_project.domain.expenses.Expense;
import com.example.final_project.domain.expenses.ExpenseId;
import com.example.final_project.infrastructure.command.expense.CreateExpenseCommand;
import com.example.final_project.infrastructure.command.expense.UpdateExpanseCommand;

public class ExpenseMapper {

    public static CreateExpenseCommand mapFromRequest(final RegisterExpenseRequest request) {
        return new CreateExpenseCommand(
                request.title(),
                request.amount()
        );
    }

    public static UpdateExpanseCommand mapFromRequest(final String expanseId, final RegisterExpenseRequest request) {
        return new UpdateExpanseCommand(
                new ExpenseId(expanseId),
                request.title(),
                request.amount()
        );
    }

    public static ExpenseResponse mapToResponse(final Expense expense) {
        return new ExpenseResponse(
                expense.title(),
                expense.expenseId().value(),
                expense.amount()
        );
    }

}

