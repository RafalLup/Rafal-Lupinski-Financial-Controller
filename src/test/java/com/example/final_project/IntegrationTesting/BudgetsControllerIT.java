package com.example.final_project.IntegrationTesting;

import com.example.final_project.api.BudgetApi;
import com.example.final_project.api.requests.RegisterBudgetRequest;
import com.example.final_project.api.responses.BudgetResponse;
import com.example.final_project.config.TestContainerConfig;
import com.example.final_project.config.TestDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.example.final_project.config.TestHelper.registerNewBudgetWithReturn;
import static com.example.final_project.config.TestHelper.registerNewUserWithReturn;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class BudgetsControllerIT extends TestContainerConfig {

    @Autowired
    private WebTestClient webClient;


    @BeforeEach
    public void shouldRegisterUser() {
        registerNewUserWithReturn(TestDataProvider.USERNAME, TestDataProvider.PASSWORD, "RAAASDFFAL@WP.PL");
    }

    @Test
    void shouldRegisterNewBudget() {
        RegisterBudgetRequest request = new RegisterBudgetRequest(TestDataProvider.TITLE_BUDGET_OR_EXPENSE, TestDataProvider.BUDGET_LIMIT, TestDataProvider.TYPE_OF_BUDGET, TestDataProvider.BUDGET_MAX_SIMPLE_EXPENSE);


        var responseSpec = webClient.post().uri(BudgetApi.BUDGETS_BASE_PATH)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(TestDataProvider.USERNAME, TestDataProvider.PASSWORD))
                .bodyValue(request).exchange().expectStatus().is2xxSuccessful().expectBody(RegisterBudgetRequest.class).returnResult().getResponseBody();


        assertThat(responseSpec.limit()).isEqualTo(TestDataProvider.BUDGET_LIMIT);
        assertThat(responseSpec.typeOfBudget()).isEqualTo(TestDataProvider.TYPE_OF_BUDGET);
        assertThat(responseSpec.maxSingleExpense()).isEqualTo(TestDataProvider.BUDGET_MAX_SIMPLE_EXPENSE);
    }

    @Test
    void shouldDeleteBudget() {
        BudgetResponse responseDto = registerNewBudgetWithReturn(TestDataProvider.TITLE_BUDGET_OR_EXPENSE, TestDataProvider.BUDGET_LIMIT, TestDataProvider.TYPE_OF_BUDGET, TestDataProvider.BUDGET_MAX_SIMPLE_EXPENSE);
        var budgetId = responseDto.budgetId();

        var responseSpec = webClient.delete()
                .uri(BudgetApi.GET_BUDGET_BASE_PATH_PLUS_SLASH + budgetId)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(TestDataProvider.USERNAME, TestDataProvider.PASSWORD))
                .exchange().expectStatus().is2xxSuccessful().expectBody().returnResult().getResponseBody();

        assertThat(responseSpec).isNullOrEmpty();
    }

    @Test
    void shouldPutBudget() {
        BudgetResponse responseDto = registerNewBudgetWithReturn(TestDataProvider.TITLE_BUDGET_OR_EXPENSE, TestDataProvider.BUDGET_LIMIT, TestDataProvider.TYPE_OF_BUDGET, TestDataProvider.BUDGET_MAX_SIMPLE_EXPENSE);
        String budgetId = String.valueOf(responseDto.budgetId());


        RegisterBudgetRequest request = new RegisterBudgetRequest(TestDataProvider.UPDATE_TITLE_FOR_BUDGET_OR_EXPENSE,
                TestDataProvider.UPDATE_BUDGET_LIMIT,
                TestDataProvider.TYPE_OF_BUDGET,
                TestDataProvider.UPDATE_MAX_SIMPLE_EXPENSE);

        var resposeSpec = webClient.put().uri(BudgetApi.GET_BUDGET_BASE_PATH_PLUS_SLASH + budgetId)
                .headers(httpHeaders -> httpHeaders.setBasicAuth(TestDataProvider.USERNAME, TestDataProvider.PASSWORD))
                .bodyValue(request).exchange().expectStatus().is2xxSuccessful().expectBody(RegisterBudgetRequest.class).returnResult().getResponseBody();

        assertThat(resposeSpec.limit()).isEqualTo(request.limit());
        assertThat(resposeSpec.typeOfBudget()).isEqualTo(request.typeOfBudget());
        assertThat(resposeSpec.maxSingleExpense()).isEqualTo(request.maxSingleExpense());
    }


}
