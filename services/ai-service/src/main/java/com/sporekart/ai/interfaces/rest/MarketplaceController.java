package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.FeatureFlagService;
import com.sporekart.ai.application.service.MarketplaceService;
import com.sporekart.ai.application.service.VendorService;
import com.sporekart.ai.common.exception.MarketplaceException;
import com.sporekart.ai.domain.model.CommissionRule;
import com.sporekart.ai.domain.model.Settlement;
import com.sporekart.ai.domain.model.Vendor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/vendors")
@Tag(name = "Marketplace", description = "Marketplace vendor management and commission endpoints")
public class MarketplaceController {
    private final VendorService vendorService;
    private final MarketplaceService marketplaceService;
    private final FeatureFlagService featureFlagService;

    public MarketplaceController(VendorService vendorService, MarketplaceService marketplaceService,
            FeatureFlagService featureFlagService) {
        this.vendorService = vendorService;
        this.marketplaceService = marketplaceService;
        this.featureFlagService = featureFlagService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a vendor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vendor registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Vendor register(@RequestBody Map<String, String> payload) {
        if (!featureFlagService.getFlags().getOrDefault("Vendor Registration Enabled", false)) {
            throw new MarketplaceException("Vendor registration is disabled");
        }
        return vendorService.registerVendor(payload.getOrDefault("name", "vendor"),
                payload.getOrDefault("gstNumber", "GST123"),
                payload.getOrDefault("bankAccount", "BANK001"));
    }

    @GetMapping
    @Operation(summary = "List vendors")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of vendors"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Vendor> vendors() {
        return vendorService.listVendors();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vendor details")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vendor details"),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Vendor vendor(@PathVariable UUID id) {
        return vendorService.listVendors().stream().filter(item -> item.getId().equals(id)).findFirst()
                .orElseThrow(() -> new MarketplaceException("Vendor not found"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vendor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vendor updated"),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Vendor updateVendor(@PathVariable UUID id, @RequestBody Map<String, String> payload) {
        return vendorService.listVendors().stream().filter(item -> item.getId().equals(id)).findFirst()
                .orElseThrow(() -> new MarketplaceException("Vendor not found"));
    }

    @PostMapping("/approve")
    @Operation(summary = "Approve a vendor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vendor approved"),
        @ApiResponse(responseCode = "404", description = "Vendor not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Vendor approve(@RequestBody Map<String, UUID> payload) {
        return vendorService.approveVendor(payload.get("vendorId"));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Vendor dashboard")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Dashboard summary"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> dashboard() {
        return Map.of("vendors", vendorService.listVendors().size(), "flags", featureFlagService.getFlags());
    }

    @GetMapping("/products")
    @Operation(summary = "List vendor products")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of products"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> products() {
        return Map.of("products", List.of());
    }

    @GetMapping("/settlements")
    @Operation(summary = "List settlements")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of settlements"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Settlement> settlements() {
        return marketplaceService.listSettlements();
    }

    @GetMapping("/marketplace/products")
    @Operation(summary = "List marketplace products")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of marketplace products"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Map<String, Object>> marketplaceProducts() {
        return List.of(Map.of("product", "sample-product"));
    }

    @GetMapping("/marketplace/vendors")
    @Operation(summary = "List marketplace vendors")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of marketplace vendors"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Vendor> marketplaceVendors() {
        return marketplaceService.listMarketplaceVendors();
    }

    @GetMapping("/marketplace/search")
    @Operation(summary = "Search marketplace")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> marketplaceSearch() {
        return Map.of("results", List.of());
    }

    @GetMapping("/marketplace/reviews")
    @Operation(summary = "List marketplace reviews")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of reviews"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<Map<String, Object>> marketplaceReviews() {
        return List.of(Map.of("rating", 5));
    }

    @GetMapping("/commission/rules")
    @Operation(summary = "List commission rules")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of commission rules"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public List<CommissionRule> commissionRules() {
        return marketplaceService.listCommissionRules();
    }
}
