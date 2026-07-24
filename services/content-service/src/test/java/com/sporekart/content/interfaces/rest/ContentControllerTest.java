package com.sporekart.content.interfaces.rest;

import com.sporekart.content.application.dto.ReviewResponse;
import com.sporekart.content.application.service.ContentService;
import com.sporekart.content.domain.model.ReviewStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContentController.class)
class ContentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContentService contentService;

    @Test
    void createReviewEndpointReturnsCreated() throws Exception {
        when(contentService.createReview(any())).thenReturn(
                new ReviewResponse("id", "prod-1", "cust-1", 5, "Great", "Good",
                        ReviewStatus.PENDING, null, null, Instant.now(), Instant.now()));

        mockMvc.perform(post("/reviews")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"productId":"prod-1","customerId":"cust-1","rating":5,"title":"Great","content":"Good"}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void getProductReviewsEndpointReturnsOk() throws Exception {
        when(contentService.getProductReviews("prod-1")).thenReturn(List.of());

        mockMvc.perform(get("/reviews/product/prod-1")
                        .with(user("customer")))
                .andExpect(status().isOk());
    }

    @Test
    void getCustomerReviewsEndpointReturnsOk() throws Exception {
        when(contentService.getCustomerReviews("cust-1")).thenReturn(List.of());

        mockMvc.perform(get("/reviews/customer/cust-1")
                        .with(user("customer")))
                .andExpect(status().isOk());
    }

    @Test
    void moderateReviewEndpointReturnsOk() throws Exception {
        when(contentService.moderateReview(eq("id"), eq(ReviewStatus.APPROVED), eq("mod-1")))
                .thenReturn(new ReviewResponse("id", "prod-1", "cust-1", 5, "Great", "Good",
                        ReviewStatus.APPROVED, "mod-1", Instant.now(), Instant.now(), Instant.now()));

        mockMvc.perform(put("/reviews/id/moderate")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN"))
                        .param("status", "APPROVED")
                        .param("moderatorId", "mod-1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReviewEndpointReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/reviews/id")
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }
}
