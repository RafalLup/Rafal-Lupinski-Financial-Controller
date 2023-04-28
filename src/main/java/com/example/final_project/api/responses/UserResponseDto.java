package com.example.final_project.api.responses;

import com.example.final_project.domain.users.BudgetAppUser;

public record UserResponseDto(
String userName,
String email,
String userId
) {
static public UserResponseDto formDomain(BudgetAppUser user){
return new UserResponseDto(
        user.getUsername(),
        user.email(),
        user.userId().value());
}
}
