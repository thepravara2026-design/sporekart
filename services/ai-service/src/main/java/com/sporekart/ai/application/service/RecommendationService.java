package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.Recommendation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RecommendationService {
    public List<Recommendation> recommend(String category) {
        return List.of(
                new Recommendation(UUID.randomUUID(), category, "Suggested based on " + category),
                new Recommendation(UUID.randomUUID(), category, "Trending in " + category));
    }
}

