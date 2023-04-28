package com.example.final_project.infrastructure.service;

import com.example.final_project.domain.users.BudgetAppUser;
import com.example.final_project.domain.users.UserId;
import com.example.final_project.exceptions.UnableToRegisterException;
import com.example.final_project.infrastructure.repository.MongoUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class MongoUserRegistrationService implements UserRegistrationService {

    private final MongoUserRepository repository;
    private final Supplier<UserId> userIdSupplier;
    private final PasswordEncoder encoder;


    public MongoUserRegistrationService(final MongoUserRepository repository, final Supplier<UserId> userIdSupplier, final PasswordEncoder encoder) {
        this.repository = repository;
        this.userIdSupplier = userIdSupplier;
        this.encoder = encoder;
    }

    @Override
    public BudgetAppUser registerNewUser(final String userName, final String rawPassword, final String email, final List<String> roles) {
        if (repository.existsByEmailOrName(email, userName)) {
            throw new UnableToRegisterException();

        }
        final BudgetAppUser budgetAppUser = new BudgetAppUser(userIdSupplier.get(), userName, email, encoder.encode(rawPassword), roles, true);
        repository.save(budgetAppUser);
        return budgetAppUser;
    }


}
