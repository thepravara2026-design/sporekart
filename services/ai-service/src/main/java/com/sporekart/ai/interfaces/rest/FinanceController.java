package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.FinanceService;
import com.sporekart.ai.domain.model.BalanceSheetReport;
import com.sporekart.ai.domain.model.FinanceAccount;
import com.sporekart.ai.domain.model.JournalEntry;
import com.sporekart.ai.domain.model.ProfitLossReport;
import com.sporekart.ai.domain.model.TrialBalanceReport;
import com.sporekart.ai.interfaces.rest.dto.AddJournalLineRequest;
import com.sporekart.ai.interfaces.rest.dto.AccountResponse;
import com.sporekart.ai.interfaces.rest.dto.BalanceSheetResponse;
import com.sporekart.ai.interfaces.rest.dto.ChartOfAccountsResponse;
import com.sporekart.ai.interfaces.rest.dto.CreateAccountRequest;
import com.sporekart.ai.interfaces.rest.dto.CreateJournalRequest;
import com.sporekart.ai.interfaces.rest.dto.JournalLineResponse;
import com.sporekart.ai.interfaces.rest.dto.JournalResponse;
import com.sporekart.ai.interfaces.rest.dto.PostJournalResponse;
import com.sporekart.ai.interfaces.rest.dto.ProfitLossResponse;
import com.sporekart.ai.interfaces.rest.dto.TrialBalanceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/finance")
@Tag(name = "Finance", description = "AI service finance and reporting APIs")
public class FinanceController {
    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @Operation(summary = "Create a finance account", description = "Creates a finance account for AI-driven accounting operations.")
    @PostMapping("/accounts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Account created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest payload) {
        FinanceAccount account = financeService.createAccount(
                payload.getCode(), payload.getName(), payload.getType(),
                payload.getCurrency(), payload.getInitialBalance());
        return new AccountResponse(account.getId(), account.getAccountCode(), account.getAccountName());
    }

    @Operation(summary = "List chart of accounts", description = "Returns the list of finance accounts and the total count.")
    @GetMapping("/accounts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Chart of accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ChartOfAccountsResponse getChartOfAccounts() {
        List<AccountResponse> accounts = financeService.getChartOfAccounts().stream()
                .map(account -> new AccountResponse(account.getId(), account.getAccountCode(),
                        account.getAccountName()))
                .toList();
        return new ChartOfAccountsResponse(accounts, accounts.size());
    }

    @Operation(summary = "Create a journal entry", description = "Creates a draft journal entry for finance transaction recording.")
    @PostMapping("/journal")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Journal entry created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public JournalResponse createJournal(@Valid @RequestBody CreateJournalRequest payload) {
        JournalEntry entry = financeService.createJournal(
                LocalDate.parse(payload.getDate()),
                payload.getDescription(),
                payload.getRefType(),
                UUID.fromString(payload.getRefId()));
        return new JournalResponse(entry.getId(), entry.getJournalNumber(), entry.getStatus());
    }

    @Operation(summary = "Add a journal line", description = "Adds a debit or credit line to an existing journal.")
    @PostMapping("/journal/{journalId}/lines")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Journal line added"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "404", description = "Journal not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public JournalLineResponse addJournalLine(@PathVariable UUID journalId,
            @Valid @RequestBody AddJournalLineRequest payload) {
        BigDecimal debit = payload.getDebit();
        BigDecimal credit = payload.getCredit();
        UUID accountId = UUID.fromString(payload.getAccountId());
        JournalEntry updated = financeService.addJournalLine(journalId, accountId, debit, credit,
                payload.getDescription());
        return new JournalLineResponse(updated.getId(), updated.getTotalDebit(), updated.getTotalCredit());
    }

    @Operation(summary = "Post a journal entry", description = "Posts a balanced journal entry to ledger and updates account balances.")
    @PostMapping("/journal/{journalId}/post")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Journal posted"),
        @ApiResponse(responseCode = "400", description = "Journal not balanced", content = @Content),
        @ApiResponse(responseCode = "404", description = "Journal not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public PostJournalResponse postJournal(@PathVariable UUID journalId, @RequestParam UUID userId) {
        financeService.postJournal(journalId, userId);
        return new PostJournalResponse(true, "Journal posted successfully");
    }

    @Operation(summary = "Get trial balance", description = "Returns trial balance totals and whether debit equals credit.")
    @GetMapping("/reports/trial-balance")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Trial balance report"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public TrialBalanceResponse getTrialBalance(@RequestParam(required = false) LocalDate asOf) {
        if (asOf == null)
            asOf = LocalDate.now();
        TrialBalanceReport report = financeService.getTrialBalance(asOf);
        return new TrialBalanceResponse(report.getTotalDebit(), report.getTotalCredit(), report.isBalanced(),
                report.getReportDate());
    }

    @Operation(summary = "Get profit and loss", description = "Returns revenue, expenses, and net profit for the requested period.")
    @GetMapping("/reports/profit-loss")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profit and loss report"),
        @ApiResponse(responseCode = "400", description = "Invalid date range", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ProfitLossResponse getProfitAndLoss(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        ProfitLossReport report = financeService.getProfitAndLoss(startDate, endDate);
        return new ProfitLossResponse(report.getRevenue(), report.getExpenses(), report.getNetProfit(),
                report.getPeriod());
    }

    @Operation(summary = "Get balance sheet", description = "Returns assets, liabilities, and equity for the given date.")
    @GetMapping("/reports/balance-sheet")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Balance sheet report"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public BalanceSheetResponse getBalanceSheet(@RequestParam(required = false) LocalDate asOf) {
        if (asOf == null)
            asOf = LocalDate.now();
        BalanceSheetReport report = financeService.getBalanceSheet(asOf);
        return new BalanceSheetResponse(report.getAssets(), report.getLiabilities(), report.getEquity(),
                report.getReportDate());
    }
}
