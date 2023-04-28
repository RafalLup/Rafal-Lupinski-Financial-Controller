package com.example.final_project.config;

import com.example.final_project.api.BudgetApi;
import com.example.final_project.api.UserApi;
import com.example.final_project.api.requests.RegisterBudgetRequest;
import com.example.final_project.api.requests.UserRegistrationRequest;
import com.example.final_project.api.responses.BudgetResponse;
import com.example.final_project.api.responses.UserResponseDto;
import com.example.final_project.domain.budgets.TypeOfBudget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class TestHelper {
    @Autowired
    private static WebTestClient webClient;
    public static BudgetResponse registerNewBudgetWithReturn(String title, BigDecimal limit, TypeOfBudget typeOfBudget, BigDecimal maxSimpleExpense) {
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

    public static UserResponseDto registerNewUserWithReturn(String userName, String password, String email) {
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
