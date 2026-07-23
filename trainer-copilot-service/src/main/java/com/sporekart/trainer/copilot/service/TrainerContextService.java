package com.sporekart.trainer.copilot.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sporekart.copilot.UserContext;

@Service
public class TrainerContextService {

    private static final Logger log = LoggerFactory.getLogger(TrainerContextService.class);

    private final Map<String, List<Map<String, Object>>> conversationHistories = new ConcurrentHashMap<>();
    private final Map<String, UserContext> trainerProfiles = new ConcurrentHashMap<>();
    private final Map<String, Map<String, Object>> sessionContexts = new ConcurrentHashMap<>();

    private static final int MAX_HISTORY_SIZE = 100;

    public void storeMessage(String sessionId, String role, String message, Map<String, Object> metadata) {
        List<Map<String, Object>> history = conversationHistories.computeIfAbsent(sessionId, k -> Collections.synchronizedList(new ArrayList<>()));
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("role", role);
        entry.put("message", message);
        entry.put("metadata", metadata != null ? metadata : Collections.emptyMap());
        entry.put("timestamp", System.currentTimeMillis());
        history.add(entry);
        if (history.size() > MAX_HISTORY_SIZE) {
            history.remove(0);
        }
        log.debug("Message stored for sessionId='{}', role='{}'", sessionId, role);
    }

    public List<Map<String, Object>> getConversationHistory(String sessionId) {
        List<Map<String, Object>> history = conversationHistories.get(sessionId);
        if (history == null) {
            log.debug("No conversation history found for sessionId='{}'", sessionId);
            return Collections.emptyList();
        }
        log.debug("Retrieved {} messages for sessionId='{}'", history.size(), sessionId);
        return Collections.unmodifiableList(new ArrayList<>(history));
    }

    public void clearConversationHistory(String sessionId) {
        conversationHistories.remove(sessionId);
        log.info("Conversation history cleared for sessionId='{}'", sessionId);
    }

    public void registerTrainerProfile(String trainerId, UserContext userContext) {
        trainerProfiles.put(trainerId, userContext);
        log.info("Trainer profile registered: trainerId='{}', userId='{}'", trainerId, userContext.userId());
    }

    public UserContext getTrainerProfile(String trainerId) {
        UserContext profile = trainerProfiles.get(trainerId);
        if (profile == null) {
            log.warn("Trainer profile not found for trainerId='{}'", trainerId);
        }
        return profile;
    }

    public void updateTrainerProfile(String trainerId, UserContext userContext) {
        trainerProfiles.put(trainerId, userContext);
        log.info("Trainer profile updated: trainerId='{}'", trainerId);
    }

    public void removeTrainerProfile(String trainerId) {
        trainerProfiles.remove(trainerId);
        log.info("Trainer profile removed: trainerId='{}'", trainerId);
    }

    public void setSessionContext(String sessionId, String key, Object value) {
        Map<String, Object> context = sessionContexts.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
        context.put(key, value);
        log.debug("Session context updated: sessionId='{}', key='{}'", sessionId, key);
    }

    public Object getSessionContext(String sessionId, String key) {
        Map<String, Object> context = sessionContexts.get(sessionId);
        if (context == null) {
            return null;
        }
        return context.get(key);
    }

    public Map<String, Object> getAllSessionContext(String sessionId) {
        Map<String, Object> context = sessionContexts.get(sessionId);
        if (context == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(new LinkedHashMap<>(context));
    }

    public void clearSessionContext(String sessionId) {
        sessionContexts.remove(sessionId);
        log.info("Session context cleared for sessionId='{}'", sessionId);
    }

    public void cleanupSession(String sessionId) {
        clearConversationHistory(sessionId);
        clearSessionContext(sessionId);
        log.info("Session cleaned up: sessionId='{}'", sessionId);
    }

    public int getActiveSessionCount() {
        return sessionContexts.size();
    }

    public int getRegisteredTrainerCount() {
        return trainerProfiles.size();
    }
}
