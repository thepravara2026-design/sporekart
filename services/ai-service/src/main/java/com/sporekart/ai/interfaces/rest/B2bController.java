package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.B2bService;
import com.sporekart.ai.application.service.FeatureFlagService;
import com.sporekart.ai.common.exception.BulkOrderException;
import com.sporekart.ai.common.exception.CreditLimitException;
import com.sporekart.ai.common.exception.DealerException;
import com.sporekart.ai.common.exception.DistributorException;
import com.sporekart.ai.common.exception.PricingException;
import com.sporekart.ai.common.exception.QuotationException;
import com.sporekart.ai.domain.model.BulkOrder;
import com.sporekart.ai.domain.model.CorporateAccount;
import com.sporekart.ai.domain.model.CreditAccount;
import com.sporekart.ai.domain.model.Dealer;
import com.sporekart.ai.domain.model.Distributor;
import com.sporekart.ai.domain.model.PurchaseAgreement;
import com.sporekart.ai.domain.model.Quotation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping
@Tag(name = "B2B Commerce", description = "B2B commerce, dealer, distributor, quotation, and bulk order endpoints")
public class B2bController {
    private final B2bService b2bService;
    private final FeatureFlagService featureFlagService;

    public B2bController(B2bService b2bService, FeatureFlagService featureFlagService) {
        this.b2bService = b2bService;
        this.featureFlagService = featureFlagService;
    }

    @PostMapping("/dealers/register")
    @Operation(summary = "Register a dealer")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dealer registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Dealer registerDealer(@RequestBody Map<String, String> payload) {
        if (!featureFlagService.getFlags().getOrDefault("Dealer Portal Enabled", false)) {
            throw new DealerException("Dealer portal is disabled");
        }
        return b2bService.registerDealer(payload.getOrDefault("name", "dealer"),
                payload.getOrDefault("gstNumber", "GST123"));
    }

    @GetMapping("/dealers")
    @Operation(summary = "List dealers")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of dealers"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Dealer> dealers() {
        return b2bService.listDealers();
    }

    @GetMapping("/dealers/dashboard")
    @Operation(summary = "Dealer dashboard")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dealer dashboard"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> dealerDashboard() {
        return Map.of("dealers", b2bService.listDealers().size(), "flags", featureFlagService.getFlags());
    }

    @PostMapping("/distributors/register")
    @Operation(summary = "Register a distributor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Distributor registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Distributor registerDistributor(@RequestBody Map<String, String> payload) {
        if (!featureFlagService.getFlags().getOrDefault("Distributor Portal Enabled", false)) {
            throw new DistributorException("Distributor portal is disabled");
        }
        return b2bService.registerDistributor(payload.getOrDefault("name", "distributor"),
                payload.getOrDefault("region", "north"));
    }

    @GetMapping("/distributors")
    @Operation(summary = "List distributors")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of distributors"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Distributor> distributors() {
        return b2bService.listDistributors();
    }

    @GetMapping("/corporate-accounts")
    @Operation(summary = "List corporate accounts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of corporate accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<CorporateAccount> corporateAccounts() {
        return b2bService.listCorporateAccounts();
    }

    @PostMapping("/quotations")
    @Operation(summary = "Create a quotation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quotation created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Quotation createQuotation(@RequestBody Map<String, String> payload) {
        if (!featureFlagService.getFlags().getOrDefault("Quotation Engine Enabled", false)) {
            throw new QuotationException("Quotation engine is disabled");
        }
        return b2bService.createQuotation(payload.getOrDefault("reference", "Q-1"),
                UUID.fromString(payload.getOrDefault("dealerId", UUID.randomUUID().toString())));
    }

    @GetMapping("/quotations")
    @Operation(summary = "List quotations")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of quotations"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Quotation> quotations() {
        return b2bService.listQuotations();
    }

    @PutMapping("/quotations/{id}")
    @Operation(summary = "Update a quotation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Quotation updated"),
        @ApiResponse(responseCode = "404", description = "Quotation not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Quotation updateQuotation(@RequestBody Map<String, String> payload) {
        return b2bService.listQuotations().stream().findFirst()
                .orElseThrow(() -> new QuotationException("No quotations available"));
    }

    @PostMapping("/bulk-orders")
    @Operation(summary = "Create a bulk order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bulk order created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public BulkOrder createBulkOrder(@RequestBody Map<String, String> payload) {
        if (Integer.parseInt(payload.getOrDefault("minimumQuantity", "100")) < 100) {
            throw new BulkOrderException("Minimum order quantity must be at least 100");
        }
        return b2bService.createBulkOrder(payload.getOrDefault("reference", "BO-1"),
                Integer.parseInt(payload.getOrDefault("minimumQuantity", "100")));
    }

    @GetMapping("/bulk-orders")
    @Operation(summary = "List bulk orders")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of bulk orders"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<BulkOrder> bulkOrders() {
        return b2bService.listBulkOrders();
    }

    @GetMapping("/credit-accounts")
    @Operation(summary = "List credit accounts")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of credit accounts"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<CreditAccount> creditAccounts() {
        return b2bService.listCreditAccounts();
    }

    @PostMapping("/credit-accounts")
    @Operation(summary = "Create a credit account")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Credit account created"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public CreditAccount createCreditAccount(@RequestBody Map<String, String> payload) {
        if (!featureFlagService.getFlags().getOrDefault("Credit System Enabled", false)) {
            throw new CreditLimitException("Credit system is disabled");
        }
        return b2bService.createCreditAccount(payload.getOrDefault("customerName", "customer"),
                new BigDecimal(payload.getOrDefault("creditLimit", "10000")));
    }

    @GetMapping("/purchase-agreements")
    @Operation(summary = "List purchase agreements")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of purchase agreements"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<PurchaseAgreement> purchaseAgreements() {
        return b2bService.listPurchaseAgreements();
    }

    @GetMapping("/sales-territories")
    @Operation(summary = "List sales territories")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sales territories"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> salesTerritories() {
        return Map.of("territories", List.of("North", "South"));
    }

    @GetMapping("/b2b/pricing")
    @Operation(summary = "Get B2B pricing")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "B2B pricing"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> pricing() {
        if (!featureFlagService.getFlags().getOrDefault("B2B Enabled", false)) {
            throw new PricingException("B2B pricing is disabled");
        }
        return Map.of("pricing", "tier-based");
    }
}
