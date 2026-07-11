package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.EmbeddingRequest;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {
    public String generateEmbedding(EmbeddingRequest request) {
        return "embedding:" + request.getText();
    }
}
