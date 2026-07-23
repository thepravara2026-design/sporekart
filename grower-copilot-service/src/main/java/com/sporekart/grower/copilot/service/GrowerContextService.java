package com.sporekart.grower.copilot.service;

import com.sporekart.grower.copilot.domain.GrowerProfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GrowerContextService {

    private static final Logger log = LoggerFactory.getLogger(GrowerContextService.class);
    private static final int MAX_HISTORY = 100;

    private final Map<String, GrowerProfile> growerProfiles = new ConcurrentHashMap<>();
    private final Map<String, List<Map<String, String>>> conversationHistory = new ConcurrentHashMap<>();

    public void registerGrower(GrowerProfile profile) {
        growerProfiles.put(profile.growerId(), profile);
        log.info("Registered grower profile: {}", profile.growerId());
    }

    public Optional<GrowerProfile> getGrowerProfile(String growerId) {
        return Optional.ofNullable(growerProfiles.get(growerId));
    }

    public GrowerProfile updateGrowerProfile(GrowerProfile profile) {
        growerProfiles.put(profile.growerId(), profile);
        log.debug("Updated grower profile: {}", profile.growerId());
        return profile;
    }

    public void addConversationMessage(String sessionId, String role, String content) {
        List<Map<String, String>> history = conversationHistory
                .computeIfAbsent(sessionId, k -> new ArrayList<>());
        synchronized (history) {
            if (history.size() >= MAX_HISTORY) {
                history.remove(0);
            }
            history.add(Map.of("role", role, "content", content));
        }
    }

    public List<Map<String, String>> getConversationHistory(String sessionId) {
        return conversationHistory.getOrDefault(sessionId, List.of());
    }

    public void clearConversationHistory(String sessionId) {
        conversationHistory.remove(sessionId);
        log.debug("Cleared conversation history for session {}", sessionId);
    }

    public int getHistorySize(String sessionId) {
        return conversationHistory.getOrDefault(sessionId, List.of()).size();
    }
}
