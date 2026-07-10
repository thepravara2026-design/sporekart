package com.sporekart.identity.domain.repository;

import com.sporekart.identity.domain.model.UserAccount;

import java.util.Optional;

public interface UserRepositoryPort {
    UserAccount save(UserAccount userAccount);

    Optional<UserAccount> findById(String id);

    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
