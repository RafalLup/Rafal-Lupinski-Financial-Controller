package com.example.final_project.infrastructure.service;

import com.example.final_project.infrastructure.repository.MongoUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MongoUserDetailsService implements UserDetailsService {
    private final MongoUserRepository repository;

    public MongoUserDetailsService(final MongoUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return repository.findUserByName(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
