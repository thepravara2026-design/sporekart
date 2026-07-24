package com.sporekart.content.interfaces.rest;

import com.sporekart.content.application.dto.CreateReviewRequest;
import com.sporekart.content.application.dto.ReviewResponse;
import com.sporekart.content.application.service.ContentService;
import com.sporekart.content.domain.model.ReviewStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contentService.createReview(request));
    }

    @PutMapping("/{id}/moderate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReviewResponse> moderateReview(
            @PathVariable String id,
            @RequestParam ReviewStatus status,
            @RequestParam String moderatorId) {
        return ResponseEntity.ok(contentService.moderateReview(id, status, moderatorId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable String productId) {
        return ResponseEntity.ok(contentService.getProductReviews(productId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ReviewResponse>> getCustomerReviews(@PathVariable String customerId) {
        return ResponseEntity.ok(contentService.getCustomerReviews(customerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        contentService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
