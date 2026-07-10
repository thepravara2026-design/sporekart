package com.sporekart.identity.domain.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class UserAccount {
    private final String id;
    private final String email;
    private final String phone;
    private final String passwordHash;
    private final String firstName;
    private final String lastName;
    private final UserStatus status;
    private final boolean emailVerified;
    private final boolean phoneVerified;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant lastLoginAt;
    private final boolean deleted;
    private final Set<RoleType> roles;

    public UserAccount(String id, String email, String phone, String passwordHash, String firstName, String lastName,
            UserStatus status, boolean emailVerified, boolean phoneVerified, Instant createdAt,
            Instant updatedAt, Instant lastLoginAt, boolean deleted, Set<RoleType> roles) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.emailVerified = emailVerified;
        this.phoneVerified = phoneVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
        this.deleted = deleted;
        this.roles = new HashSet<>(roles);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UserStatus getStatus() {
        return status;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Set<RoleType> getRoles() {
        return Set.copyOf(roles);
    }
}
