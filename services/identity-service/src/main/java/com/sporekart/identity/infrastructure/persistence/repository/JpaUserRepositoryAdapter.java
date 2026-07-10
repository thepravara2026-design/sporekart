package com.sporekart.identity.infrastructure.persistence.repository;

import com.sporekart.identity.domain.model.UserAccount;
import com.sporekart.identity.domain.repository.UserRepositoryPort;
import com.sporekart.identity.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {
    private final SpringDataUserRepository springDataUserRepository;

    public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository) {
        this.springDataUserRepository = springDataUserRepository;
    }

    @Override
    public UserAccount save(UserAccount userAccount) {
        UserEntity entity = new UserEntity();
        entity.setId(userAccount.getId());
        entity.setEmail(userAccount.getEmail());
        entity.setPhone(userAccount.getPhone());
        entity.setPasswordHash(userAccount.getPasswordHash());
        entity.setFirstName(userAccount.getFirstName());
        entity.setLastName(userAccount.getLastName());
        entity.setStatus(userAccount.getStatus());
        entity.setEmailVerified(userAccount.isEmailVerified());
        entity.setPhoneVerified(userAccount.isPhoneVerified());
        entity.setCreatedAt(userAccount.getCreatedAt());
        entity.setUpdatedAt(userAccount.getUpdatedAt());
        entity.setLastLoginAt(userAccount.getLastLoginAt());
        entity.setDeleted(userAccount.isDeleted());
        return toDomain(springDataUserRepository.save(entity));
    }

    @Override
    public Optional<UserAccount> findById(String id) {
        return springDataUserRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return springDataUserRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public Optional<UserAccount> findByPhone(String phone) {
        return springDataUserRepository.findByPhone(phone).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataUserRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return springDataUserRepository.existsByPhone(phone);
    }

    private UserAccount toDomain(UserEntity entity) {
        return new UserAccount(
                entity.getId(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getPasswordHash(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getStatus(),
                entity.isEmailVerified(),
                entity.isPhoneVerified(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getLastLoginAt(),
                entity.isDeleted(),
                java.util.Collections.emptySet());
    }
}
