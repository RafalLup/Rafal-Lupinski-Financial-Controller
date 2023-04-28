package com.example.final_project;

import com.example.final_project.api.BudgetApi;
import com.example.final_project.api.ExpanseApi;
import com.example.final_project.api.UserApi;
import com.example.final_project.api.requests.RegisterBudgetRequest;
import com.example.final_project.api.requests.RegisterExpenseRequest;
import com.example.final_project.api.requests.UserRegistrationRequest;
import com.example.final_project.api.responses.BudgetResponse;
import com.example.final_project.api.responses.ExpenseResponse;
import com.example.final_project.api.responses.UserResponseDto;
import com.example.final_project.config.TestContainerConfig;
import com.example.final_project.config.TestDataProvider;
import com.example.final_project.domain.budgets.BudgetId;
import com.example.final_project.domain.budgets.TypeOfBudget;
import com.example.final_project.domain.expenses.ExpenseId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ExpensesControllerIT extends TestContainerConfig {


    @Autowired
    private WebTestClient webClient;

    @BeforeEach
    public void shouldRegisterUser() {
        registerNewUserWithReturn(TestDataProvider.USERNAME, TestDataProvider.PASSWORD, "RAAASDFFAL@WP.PL");
    }


    @Test()
    void shouldRegisterNewExpense() {
        //GIVEN
        BudgetResponse responseBudget = registerNewBudgetWithReturn(
                TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
                TestDataProvider.BUDGET_LIMIT,
                TestDataProvider.TYPE_OF_BUDGET,
                TestDataProvider.BUDGET_MAX_SIMPLE_EXPENSE);

        var budgetId = new BudgetId(responseBudget.budgetId());


        RegisterExpenseRequest request = new RegisterExpenseRequest(
                TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
                TestDataProvider.EXPANSE_AMOUNT,
                budgetId.value());

        //WHEN
        var responseSpec = webClient.post()
                .uri(ExpanseApi.EXPENSES_BASE_PATH).headers(httpHeaders -> httpHeaders.setBasicAuth(TestDataProvider.USERNAME, TestDataProvider.PASSWORD))
                .bodyValue(request).exchange().expectStatus().is2xxSuccessful()
                .expectBody(RegisterExpenseRequest.class).returnResult().getResponseBody();

        //THEN
        assertThat(responseSpec.title().equals(TestDataProvider.TITLE_BUDGET_OR_EXPENSE));
        assertThat(responseSpec.amount().equals(TestDataProvider.EXPANSE_AMOUNT));
        assertThat(responseSpec).isNotNull();
    }


    @Test
    void shouldDeleteExpense() {
        //GIVEN
        BudgetResponse responseBudget = registerNewBudgetWithReturn(
                TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
                TestDataProvider.BUDGET_LIMIT,
                TestDataProvider.TYPE_OF_BUDGET,
                TestDataProvider.BUDGET_MAX_SIMPLE_EXPENSE);
        var budgetId = new BudgetId(responseBudget.budgetId());

        ExpenseResponse expenseRequest = registerNewExpenseWithReturn(
                TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
                TestDataProvider.EXPANSE_AMOUNT,
                budgetId.value());

        var expanseId = new ExpenseId(expenseRequest.expenseId());
        //WHEN
        var responseSpec = webClient.delete()
                .uri(ExpanseApi.GET_EXPENSES_BASE_PATH_PLUS_SLASH + expanseId.value())
                .headers(httpHeaders -> httpHeaders.setBasicAuth(TestDataProvider.USERNAME, TestDataProvider.PASSWORD)).exchange().expectStatus()
                .is2xxSuccessful().expectBody().returnResult().getResponseBody();
        //THEN
        assertThat(responseSpec).isNullOrEmpty();

    }

    @Test
    void shouldPutExpense() {
        BudgetResponse responseBudget = registerNewBudgetWithReturn(
                TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
                TestDataProvider.BUDGET_LIMIT,
                TestDataProvider.TYPE_OF_BUDGET,
                TestDataProvider.BUDGET_MAX_SIMPLE_EXPENSE);
        var budgetId = new BudgetId(responseBudget.budgetId());

        ExpenseResponse expenseRequest = registerNewExpenseWithReturn(
                TestDataProvider.TITLE_BUDGET_OR_EXPENSE,
                TestDataProvider.EXPANSE_AMOUNT,
                budgetId.value());

        RegisterExpenseRequest updateExpense = new RegisterExpenseRequest(
                TestDataProvider.UPDATE_TITLE_FOR_BUDGET_OR_EXPENSE,
                TestDataProvider.UPDATE_EXPANSE_AMOUNT,
                budgetId.value());

        var expanseId = new ExpenseId(expenseRequest.expenseId());

        var responseSpec = webClient.put()
                .uri(ExpanseApi.GET_EXPENSES_BASE_PATH_PLUS_SLASH + expanseId.value())
                .headers(httpHeaders -> httpHeaders.setBasicAuth(TestDataProvider.USERNAME, TestDataProvider.PASSWORD))
                .bodyValue(updateExpense).exchange().expectStatus().isOk()
                .expectBody(RegisterExpenseRequest.class).returnResult().getResponseBody();

        assertThat(responseSpec.amount()).isEqualTo(TestDataProvider.UPDATE_EXPANSE_AMOUNT);
        assertThat(responseSpec.title()).isEqualTo(TestDataProvider.UPDATE_TITLE_FOR_BUDGET_OR_EXPENSE);

    }

    BudgetResponse registerNewBudgetWithReturn(String title, BigDecimal limit, TypeOfBudget typeOfBudget, BigDecimal maxSimpleExpense) {
        RegisterBudgetRequest request = new RegisterBudgetRequest(
                title,
                limit,
                typeOfBudget,
                maxSimpleExpense);

        var responseSpec = webClient.post()
                .uri(BudgetApi.BUDGETS_BASE_PATH).headers(httpHeaders -> httpHeaders.setBasicAuth(TestDataProvider.USERNAME, TestDataProvider.PASSWORD))
                .bodyValue(request).exchange().returnResult(BudgetResponse.class).getResponseBody().blockFirst();

        return responseSpec;
    }


    ExpenseResponse registerNewExpenseWithReturn(String title, BigDecimal expectedAmount, String budgetId) {
        RegisterExpenseRequest request = new RegisterExpenseRequest(
                title,
                expectedAmount,
                new BudgetId(budgetId).value());

        var responseSpec = webClient.post()
                .uri(ExpanseApi.EXPENSES_BASE_PATH).headers(httpHeaders -> httpHeaders.setBasicAuth(TestDataProvider.USERNAME, TestDataProvider.PASSWORD))
                .bodyValue(request).exchange().returnResult(ExpenseResponse.class).getResponseBody().blockFirst();
        return responseSpec;
    }

    UserResponseDto registerNewUserWithReturn(String userName, String password, String email) {
        UserRegistrationRequest request = new UserRegistrationRequest(
                userName,
                password,
                email);

        var responseSpec = webClient.post()
                .uri(UserApi.USERS_BASE_PATH).bodyValue(request).exchange()
                .returnResult(UserResponseDto.class).getResponseBody().blockFirst();

        return responseSpec;
    }


}