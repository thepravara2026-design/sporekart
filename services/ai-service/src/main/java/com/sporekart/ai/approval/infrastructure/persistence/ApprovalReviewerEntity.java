package com.sporekart.ai.approval.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_approval_reviewers")
public class ApprovalReviewerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", length = 100)
    private String userId;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "department", length = 100)
    private String department;

    @Column(name = "roles", columnDefinition = "TEXT")
    private String roles;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "max_assignments")
    private Integer maxAssignments;

    @Column(name = "current_assignments")
    private Integer currentAssignments;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "last_assigned")
    private OffsetDateTime lastAssigned;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public ApprovalReviewerEntity() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getRoles() { return roles; }
    public void setRoles(String roles) { this.roles = roles; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public Integer getMaxAssignments() { return maxAssignments; }
    public void setMaxAssignments(Integer maxAssignments) { this.maxAssignments = maxAssignments; }
    public Integer getCurrentAssignments() { return currentAssignments; }
    public void setCurrentAssignments(Integer currentAssignments) { this.currentAssignments = currentAssignments; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    public OffsetDateTime getLastAssigned() { return lastAssigned; }
    public void setLastAssigned(OffsetDateTime lastAssigned) { this.lastAssigned = lastAssigned; }
    public Boolean getIsDeleted() { return isDeleted; }
    public void setIsDeleted(Boolean isDeleted) { this.isDeleted = isDeleted; }
}
