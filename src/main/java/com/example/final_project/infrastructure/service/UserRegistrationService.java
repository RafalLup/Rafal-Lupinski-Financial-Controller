package com.example.final_project.infrastructure.service;

import com.example.final_project.domain.users.BudgetAppUser;

import java.util.List;

public interface UserRegistrationService {
    List<String> defaultRoles = List.of("USER");


    BudgetAppUser registerNewUser(String userName, String rawPassword, String email, List<String> roles);


}
