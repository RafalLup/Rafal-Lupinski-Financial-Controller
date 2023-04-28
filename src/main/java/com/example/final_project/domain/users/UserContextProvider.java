package com.example.final_project.domain.users;

import org.springframework.security.core.context.SecurityContextHolder;

public enum UserContextProvider {

    INSTANCE;

    public static BudgetAppUser getUserContext() {
        return (BudgetAppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
