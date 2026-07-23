package com.sporekart.identity.infrastructure.persistence.entity;

import com.sporekart.identity.domain.model.Device.DeviceType;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "devices")
public class DeviceEntity {
    @Id
    @Column(length = 36)
    private String deviceId;

    @Column(nullable = false, length = 36)
    private String userId;

    @Column(length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private DeviceType type;

    @Column(length = 50)
    private String os;

    @Column(length = 50)
    private String browser;

    @Column(length = 255)
    private String deviceIdentifier;

    @Column(nullable = false)
    private boolean trusted;

    @Column(nullable = false)
    private boolean suspicious;

    @Column(nullable = false)
    private Instant lastUsedAt;

    @Column(nullable = false)
    private Instant createdAt;

    public DeviceEntity() {}

    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public DeviceType getType() { return type; }
    public void setType(DeviceType type) { this.type = type; }
    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }
    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }
    public String getDeviceIdentifier() { return deviceIdentifier; }
    public void setDeviceIdentifier(String deviceIdentifier) { this.deviceIdentifier = deviceIdentifier; }
    public boolean isTrusted() { return trusted; }
    public void setTrusted(boolean trusted) { this.trusted = trusted; }
    public boolean isSuspicious() { return suspicious; }
    public void setSuspicious(boolean suspicious) { this.suspicious = suspicious; }
    public Instant getLastUsedAt() { return lastUsedAt; }
    public void setLastUsedAt(Instant lastUsedAt) { this.lastUsedAt = lastUsedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
