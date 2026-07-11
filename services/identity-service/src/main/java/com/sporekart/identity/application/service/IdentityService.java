package com.sporekart.identity.application.service;

import com.sporekart.identity.application.dto.AuthResponse;
import com.sporekart.identity.application.dto.RegisterRequest;
import com.sporekart.identity.common.exception.BusinessException;
import com.sporekart.identity.common.exception.DuplicateUserException;
import com.sporekart.identity.domain.model.RoleType;
import com.sporekart.identity.domain.model.UserAccount;
import com.sporekart.identity.domain.model.UserStatus;
import com.sporekart.identity.domain.repository.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class IdentityService {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    public IdentityService(UserRepositoryPort userRepositoryPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserAccount> getById(String id) {
        return userRepositoryPort.findById(id);
    }

    public UserAccount register(RegisterRequest request) {
        if (userRepositoryPort.existsByEmail(request.email())) {
            throw new DuplicateUserException("Email already registered");
        }
        UserAccount user = new UserAccount(
                UUID.randomUUID().toString(),
                request.email(),
                request.phone(),
                passwordEncoder.encode(request.password()),
                request.firstName(),
                request.lastName(),
                UserStatus.PENDING,
                false,
                false,
                Instant.now(),
                Instant.now(),
                null,
                false,
                Set.of(RoleType.CUSTOMER));
        return userRepositoryPort.save(user);
    }

    public AuthResponse login(String username, String password) {
        UserAccount user = userRepositoryPort.findByEmail(username)
                .orElseThrow(() -> new BusinessException("Invalid credentials"));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BusinessException("Invalid credentials");
        }
        return new AuthResponse("placeholder-access-token", "placeholder-refresh-token", "Bearer", 900L);
    }
}
