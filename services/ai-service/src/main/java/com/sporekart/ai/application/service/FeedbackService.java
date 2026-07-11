package com.sporekart.ai.application.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FeedbackService {
    public Map<String, String> submit(String type, String message) {
        return Map.of("status", "submitted", "type", type, "message", message);
    }
}

