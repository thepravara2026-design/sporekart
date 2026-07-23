package com.sporekart.copilot.context;

import com.sporekart.copilot.domain.ConversationMessage;
import com.sporekart.copilot.domain.CopilotType;
import com.sporekart.copilot.domain.SessionId;
import com.sporekart.copilot.memory.MemoryEntry;
import com.sporekart.copilot.memory.MemoryStore;
import com.sporekart.copilot.memory.MemoryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContextAssemblerImpl implements ContextAssembler {

    private static final Logger log = LoggerFactory.getLogger(ContextAssemblerImpl.class);

    private final MemoryStore memoryStore;

    public ContextAssemblerImpl(MemoryStore memoryStore) {
        this.memoryStore = memoryStore;
    }

    @Override
    public CopilotContext assembleContext(UserContext user, PageContext page, CopilotType copilotType, SessionId sessionId) {
        log.debug("Assembling context for sessionId={}, copilotType={}, userId={}", sessionId, copilotType, user.userId());

        List<ConversationMessage> recentHistory = loadRecentHistory(sessionId);
        Map<String, Object> contextualData = buildContextualData(user, page, sessionId);

        return new CopilotContext(
            user,
            page != null ? page : PageContext.empty(),
            copilotType,
            sessionId,
            recentHistory,
            contextualData,
            OffsetDateTime.now()
        );
    }

    private List<ConversationMessage> loadRecentHistory(SessionId sessionId) {
        List<MemoryEntry> sessionEntries = memoryStore.findByType(MemoryType.SESSION);
        return sessionEntries.stream()
            .filter(entry -> entry.key().startsWith("conversation:" + sessionId))
            .map(entry -> {
                if (entry.value() instanceof ConversationMessage msg) {
                    return msg;
                }
                return null;
            })
            .filter(m -> m != null)
            .sorted((a, b) -> a.timestamp().compareTo(b.timestamp()))
            .collect(Collectors.toList());
    }

    private Map<String, Object> buildContextualData(UserContext user, PageContext page, SessionId sessionId) {
        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.userId());
        data.put("sessionId", sessionId.toString());

        if (page != null) {
            if (page.pageUrl() != null && !page.pageUrl().isBlank()) data.put("pageUrl", page.pageUrl());
            if (page.pageTitle() != null && !page.pageTitle().isBlank()) data.put("pageTitle", page.pageTitle());
            if (page.section() != null && !page.section().isBlank()) data.put("section", page.section());
            if (page.entityType() != null && !page.entityType().isBlank()) data.put("entityType", page.entityType());
            if (page.entityId() != null && !page.entityId().isBlank()) data.put("entityId", page.entityId());
        }

        List<MemoryEntry> contextMemory = memoryStore.findByType(MemoryType.CONTEXT);
        for (MemoryEntry entry : contextMemory) {
            if (entry.key().startsWith("context:" + sessionId)) {
                data.put(entry.key().substring(("context:" + sessionId).length() + 1), entry.value());
            }
        }

        return Map.copyOf(data);
    }
}
