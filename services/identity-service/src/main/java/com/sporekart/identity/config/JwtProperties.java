package com.sporekart.identity.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@ConfigurationProperties(prefix = "app.security.jwt")
@Validated
public class JwtProperties {

    @NotBlank
    private String secret;

    @NotBlank
    private String issuer;

    @Positive
    private long expirationMinutes = 15;

    @Positive
    private long refreshExpirationMinutes = 10080;

    @Positive
    private int clockSkewSeconds = 30;

    @Positive
    private int blacklistSize = 10000;

    public String getSecret() { return secret; }
    public void setSecret(String secret) { this.secret = secret; }
    public String getIssuer() { return issuer; }
    public void setIssuer(String issuer) { this.issuer = issuer; }
    public long getExpirationMinutes() { return expirationMinutes; }
    public void setExpirationMinutes(long expirationMinutes) { this.expirationMinutes = expirationMinutes; }
    public long getRefreshExpirationMinutes() { return refreshExpirationMinutes; }
    public void setRefreshExpirationMinutes(long refreshExpirationMinutes) { this.refreshExpirationMinutes = refreshExpirationMinutes; }
    public int getClockSkewSeconds() { return clockSkewSeconds; }
    public void setClockSkewSeconds(int clockSkewSeconds) { this.clockSkewSeconds = clockSkewSeconds; }
    public int getBlacklistSize() { return blacklistSize; }
    public void setBlacklistSize(int blacklistSize) { this.blacklistSize = blacklistSize; }
}
