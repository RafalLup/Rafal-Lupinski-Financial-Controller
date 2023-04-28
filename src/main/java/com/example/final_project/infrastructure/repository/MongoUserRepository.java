package com.example.final_project.infrastructure.repository;

import com.example.final_project.domain.users.BudgetAppUser;
import com.example.final_project.domain.users.UserId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface MongoUserRepository extends MongoRepository<BudgetAppUser, UserId> {

    Optional<BudgetAppUser> findUserByName(String name);

    boolean existsByEmailOrName(String email, String name);
}
