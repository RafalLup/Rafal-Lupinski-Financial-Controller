package com.example.final_project.api.controllers;


import com.example.final_project.api.UserApi;
import com.example.final_project.api.requests.UserRegistrationRequest;
import com.example.final_project.api.responses.UserResponseDto;
import com.example.final_project.domain.users.BudgetAppUser;
import com.example.final_project.infrastructure.service.MongoUserDetailsService;
import com.example.final_project.infrastructure.service.UserRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(UserApi.USERS_BASE_PATH)
public class UserController {

    private final UserRegistrationService registrationService;
    private final MongoUserDetailsService detailsService;


    public UserController(UserRegistrationService registrationService, MongoUserDetailsService detailsService) {
        this.registrationService = registrationService;
        this.detailsService = detailsService;
    }


    @PostMapping
    ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRegistrationRequest request) {
        final BudgetAppUser budgetAppUser = registrationService.registerNewUser(
                request.userName(),
                request.password(),
                request.email(),
                UserRegistrationService.defaultRoles);
        return ResponseEntity.ok(UserResponseDto.formDomain(budgetAppUser));
    }

    @GetMapping(UserApi.GET_USER_BY_USERNAME)
    UserDetails getUserById(@PathVariable String userName) {
        return detailsService.loadUserByUsername(userName);
    }

}
