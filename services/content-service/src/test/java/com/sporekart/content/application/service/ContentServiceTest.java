package com.sporekart.content.application.service;

import com.sporekart.content.application.dto.CreateReviewRequest;
import com.sporekart.content.application.dto.ReviewResponse;
import com.sporekart.content.common.exception.ReviewNotFoundException;
import com.sporekart.content.domain.model.ReviewStatus;
import com.sporekart.content.domain.repository.ContentRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ContentServiceTest {

    @Test
    void createReviewPersistsValidReview() {
        ContentRepositoryPort repositoryPort = Mockito.mock(ContentRepositoryPort.class);
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ContentService service = new ContentService(repositoryPort);

        ReviewResponse response = service.createReview(
                new CreateReviewRequest("prod-1", "cust-1", 5, "Great product", "Loved it!"));

        assertNotNull(response);
        assertEquals("prod-1", response.productId());
        assertEquals(5, response.rating());
        assertEquals(ReviewStatus.PENDING, response.status());
    }

    @Test
    void moderateReviewChangesStatus() {
        ContentRepositoryPort repositoryPort = Mockito.mock(ContentRepositoryPort.class);
        var review = com.sporekart.content.domain.model.Review.create("prod-1", "cust-1", 4, "Nice", "Good");
        when(repositoryPort.findById(review.getId())).thenReturn(java.util.Optional.of(review));
        when(repositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        ContentService service = new ContentService(repositoryPort);

        ReviewResponse response = service.moderateReview(review.getId(), ReviewStatus.APPROVED, "mod-1");

        assertEquals(ReviewStatus.APPROVED, response.status());
        assertEquals("mod-1", response.moderatedBy());
        assertNotNull(response.moderatedAt());
    }

    @Test
    void moderateAlreadyModeratedReviewThrowsException() {
        ContentRepositoryPort repositoryPort = Mockito.mock(ContentRepositoryPort.class);
        var review = com.sporekart.content.domain.model.Review.create("prod-1", "cust-1", 4, "Nice", "Good");
        var moderated = review.withModeration(ReviewStatus.APPROVED, "mod-1");
        when(repositoryPort.findById(review.getId())).thenReturn(java.util.Optional.of(moderated));
        ContentService service = new ContentService(repositoryPort);

        assertThrows(IllegalStateException.class,
                () -> service.moderateReview(review.getId(), ReviewStatus.REJECTED, "mod-2"));
    }

    @Test
    void deleteReviewThrowsWhenNotFound() {
        ContentRepositoryPort repositoryPort = Mockito.mock(ContentRepositoryPort.class);
        when(repositoryPort.findById("nonexistent")).thenReturn(java.util.Optional.empty());
        ContentService service = new ContentService(repositoryPort);

        assertThrows(ReviewNotFoundException.class, () -> service.deleteReview("nonexistent"));
    }

    @Test
    void deleteReviewSucceedsForUnmoderatedReview() {
        ContentRepositoryPort repositoryPort = Mockito.mock(ContentRepositoryPort.class);
        var review = com.sporekart.content.domain.model.Review.create("prod-1", "cust-1", 3, "Okay", "Fine");
        when(repositoryPort.findById(review.getId())).thenReturn(java.util.Optional.of(review));
        ContentService service = new ContentService(repositoryPort);

        assertDoesNotThrow(() -> service.deleteReview(review.getId()));
    }
}
