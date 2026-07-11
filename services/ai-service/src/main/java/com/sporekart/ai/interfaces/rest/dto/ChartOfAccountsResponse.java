package com.sporekart.ai.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class ChartOfAccountsResponse {
    @Schema(description = "List of finance accounts")
    private List<AccountResponse> accounts;

    @Schema(description = "Total number of accounts", example = "12")
    private int total;

    public ChartOfAccountsResponse() {
    }

    public ChartOfAccountsResponse(List<AccountResponse> accounts, int total) {
        this.accounts = accounts;
        this.total = total;
    }

    public List<AccountResponse> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountResponse> accounts) {
        this.accounts = accounts;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
