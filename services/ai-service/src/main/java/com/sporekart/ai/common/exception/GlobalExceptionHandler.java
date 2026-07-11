package com.sporekart.ai.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetails> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "validation.error", ex.getMessage());
    }

    @ExceptionHandler({ AssistantException.class, KnowledgeRetrievalException.class, RecommendationException.class,
            SemanticSearchException.class, ConversationLimitException.class, VendorException.class,
            MarketplaceException.class, CommissionException.class, SettlementException.class,
            VendorApprovalException.class, VendorComplianceException.class, DealerException.class,
            DistributorException.class, QuotationException.class, BulkOrderException.class,
            CreditLimitException.class, PricingException.class, AgreementException.class,
            DeviceRegistrationException.class, OfflineSyncException.class, PushNotificationException.class,
            BiometricException.class, FileUploadException.class,
            AccountingException.class, GSTException.class, ProcurementException.class,
            SupplierException.class, WarehouseException.class, ERPIntegrationException.class,
            SynchronizationException.class })
    public ResponseEntity<ProblemDetails> handlePlatformExceptions(RuntimeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "platform.error", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "server.error", "Unexpected server error");
    }

    private ResponseEntity<ProblemDetails> buildResponse(HttpStatus status, String type, String detail) {
        Map<String, Object> extensions = new LinkedHashMap<>();
        extensions.put("timestamp", OffsetDateTime.now());
        ProblemDetails problem = new ProblemDetails(
                "/problems/" + type,
                status.getReasonPhrase(),
                status.value(),
                detail,
                null,
                OffsetDateTime.now(),
                extensions);
        return ResponseEntity.status(status).body(problem);
    }
}

