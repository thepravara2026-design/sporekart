package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public class AccountResponse {
    @Schema(description = "Unique account identifier", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Schema(description = "Ledger account code", example = "1000-CASH")
    private String code;

    @Schema(description = "Account name", example = "Cash on Hand")
    private String name;

    public AccountResponse() {
    }

    public AccountResponse(UUID id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}
