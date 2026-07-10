package com.sporekart.identity.infrastructure.persistence.entity;

import com.sporekart.identity.domain.model.Permission;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "permissions")
public class PermissionEntity {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false, length = 80)
    private Permission permission;

    public PermissionEntity() {
    }

    public PermissionEntity(Permission permission) {
        this.permission = permission;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
